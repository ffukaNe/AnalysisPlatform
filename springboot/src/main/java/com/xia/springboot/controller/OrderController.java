package com.xia.springboot.controller;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.springboot.common.Result;
import com.xia.springboot.entity.Item;
import com.xia.springboot.entity.Order;
import com.xia.springboot.entity.User;
import com.xia.springboot.exception.ServiceException;
import com.xia.springboot.mapper.ItemMapper;
import com.xia.springboot.mapper.OrderMapper;
import com.xia.springboot.mapper.UserMapper;
import com.xia.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/order")
public class OrderController {
    @Resource
    OrderMapper orderMapper;

    @Resource
    UserMapper userMapper;

    @Resource
    ItemMapper itemMapper;

    @PostMapping
    public Result<?> save(@RequestBody Order order){
        orderMapper.insert(order);
        return Result.success();
    }

    //更新订单
    @PutMapping
    public Result<?> update(@RequestBody Order order){
        orderMapper.updateById(order);
        return Result.success();
    }

    //根据订单ID删除订单
    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id){
        orderMapper.deleteById(id);
        return Result.success();
    }

    //根据订单ID查询订单
    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id){
        return Result.success(orderMapper.selectById(id));
    }

    //购买商品，新建一个订单，单独购买一个商品
    @GetMapping("/buy")
    public Result<?> buy(@RequestParam Long itemId, @RequestParam Integer userId, @RequestParam Integer count,
                         HttpServletRequest request){
        Item item = itemMapper.selectById(itemId);
        User user = userMapper.selectById(userId);
        String orderNo = IdUtil.getSnowflake().nextIdStr();
        // 先新建未支付订单
        Order order = createOrder(item, user, orderNo,count);
        // 如果是小程序，回传这个订单，进入确认订单页面
        if (StrUtil.isNotEmpty(request.getHeader("source-client"))){
            return Result.success(order);
        }
        // 如果是web端
        else {
            BigDecimal price = item.getPrice().multiply(new BigDecimal(count));
            String payUrl = "http://localhost:9090/alipay/pay?subject=" + item.getTitle() + "&traceNo=" + orderNo + "&totalAmount=" + price;
            return Result.success(payUrl);
        }
    }

    private Order createOrder(Item item, User user, String orderNo, Integer count) {
        Order order = new Order();
        // 设置商品ID
        order.setProductId(item.getId());
        // 获取商品名称
        order.setOrderName(item.getTitle());
        // 设置订单号
        order.setOrderNo(orderNo);
        // 设置订单价格
        order.setPayPrice(item.getPrice());
        // 设置邮费
        order.setPostFee(BigDecimal.ZERO);
        // 设置商品数量
        order.setCount(count);
        // 设置支付状态
        order.setState(2);
        order.setUserId(user.getId());
        order.setUsername(user.getUsername());
        order.setOrderPicture(item.getImage());
        //构建创建时间
        Date currDate = new Date();
        order.setCreateTime(currDate);
        save(order);
        return order;
    }

    @PostMapping("/buyCart")
    public Result<?> buyCart(@RequestBody List<Order> orders,@RequestParam BigDecimal totalPrice,
                             @RequestParam Integer addressId, HttpServletRequest request) {
        // 生成订单号
        String orderNo = IdUtil.getSnowflake().nextIdStr();
        // 如果是 Web 端
        if (StrUtil.isEmpty(request.getHeader("source-client"))) {
            // 生成订单名称
            String orderName = orders.get(0).getOrderName() + "等多件商品";
            // 拼接支付URL
            String payUrl = "http://localhost:9090/alipay/pay?subject=" + orderName + "&traceNo=" + orderNo + "&totalAmount=" + totalPrice;
            // 对所以加入购物车的商品进行处理，添加订单号
            for (Order order : orders) {
                order.setOrderNo(orderNo);
                // 同时也更新商品的选择数量
                orderMapper.updateById(order);
            }
            // 返回支付URL
            return Result.success(payUrl);
        }
        // 如果是手机端
        else {
            // 对所有加入购物车的商品进行处理，添加订单号，添加地址
            for (Order order : orders) {
                order.setOrderNo(orderNo);
                order.setAddressId(addressId);
                // 同时也更新商品的选择数量
                orderMapper.updateById(order);
            }
            // 回传统一的订单编号
            return Result.success(orderNo);
        }
    }

    /**
     * 加入购物车的时候，没有生成订单号，只有购买的时候才会生成
     * @param itemId 要加入购物车的物品ID
     * @param userId 用户的ID
     * @return 返回成功消息就可以了
     */
    @GetMapping("/add")
    public Result<?> addToCart(@RequestParam Long itemId,@RequestParam Integer userId,@RequestParam Integer count){
        Item item = itemMapper.selectById(itemId);
        User user = userMapper.selectById(userId);
        createOrder(item,user,"",count);
        return Result.success();
    }

    /**
     * 查询全部的订单消息
     * @param pageNum 当前的页数
     * @param pageSize 一页的大小
     * @param search 搜索的关键词
     * @return 结果集
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        //新建分页对象
        Page<Order> page = new Page<>(pageNum, pageSize);
        //新建模糊查询对象,这里有个注意事项，你模糊项查询的对应项不能为null，为null就查不出来
        //LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().like(User::getUsername, search);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.orderByDesc(Order::getCreateTime);
        if (!search.equals(""))
            queryWrapper.like(Order::getOrderName,search);
        Page<Order> userPage = orderMapper.selectPage(page, queryWrapper);
        return Result.success(userPage);
    }

    /**
     * 查询对应用户的订单信息
     * @param pageNum 当前页码
     * @param pageSize 每一页的条数
     * @param search 搜索的商品名称
     * @param state 查询订单的状态类型，0表示全部
     * @return 返回分页好的数据表
     */
    @GetMapping("/user_order")
    public Result<?> findUserOrder(@RequestParam(defaultValue = "1") Integer pageNum,
                                   @RequestParam(defaultValue = "10") Integer pageSize,
                                   @RequestParam(defaultValue = "") String search,
                                   @RequestParam(defaultValue = "0") Integer state) {
        // 从 token 获取 userID
        String userId = TokenUtils.getCurrUser();
        //新建分页对象
        Page<Order> page = new Page<>(pageNum, pageSize);
        //新建模糊查询对象,这里有个注意事项，你模糊项查询的对应项不能为null，为null就查不出来
        //LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().like(User::getUsername, search);
        LambdaQueryWrapper<Order> queryWrapper = Wrappers.lambdaQuery();
        // 给查询对象加上ID限制和日期降序
        queryWrapper.eq(Order::getUserId,userId);
        if (state == 2)
            queryWrapper.orderByDesc(Order::getCreateTime);
        else
            queryWrapper.orderByDesc(Order::getPaymentTime);
        // 不为空就给查询对象加上搜索限制,例如是否查询未支付订单，或者模糊搜索
        if (state != 0)
            queryWrapper.eq(Order::getState,state);
        if (!search.equals(""))
            queryWrapper.like(Order::getOrderName,search);
        Page<Order> userPage = orderMapper.selectPage(page, queryWrapper);
        return Result.success(userPage);
    }

    /**
     * 根据传入的订单编号查出该订单购买的商品
     */
    @GetMapping("/selectOrder")
    public Result<?> findOrderByNo(@RequestParam String orderNo){
        LambdaQueryWrapper<Order> wrapper = Wrappers.lambdaQuery();
        wrapper.eq(Order::getOrderNo,orderNo);
        List<Order> orders = orderMapper.selectList(wrapper);
        return Result.success(orders);
    }

    // 小程序端模拟支付接口，直接模拟收到微信回调更新库存等等操作
    @PostMapping("/mockPay")
    public Result<?> mockNotify(@RequestBody List<Order> orders){
        // 获取当前支付时间
        Date payment = new Date();
        // 更新订单状态
        for (Order order : orders) {
            // 先进行库存扣减，如果扣减失败，直接弹出库存不足
            if (itemMapper.updateStock(order.getProductId(),order.getCount()) <= 0){
                throw new ServiceException("407","您购买的商品中有库存不足的情况");
            }
            // 设置支付时间
            order.setPaymentTime(payment);
            // 设置支付状态
            order.setState(1);
            // 更新订单
            orderMapper.updateById(order);
        }
        return Result.success();
    }

    // 模拟发货接口
    @GetMapping("/mockSend/{id}")
    public Result<?> mockSend(@PathVariable("id") String orderNo){
        if (orderMapper.updateStateWithoutDate(orderNo,4) <= 0)
            throw new ServiceException("400","，模拟发货出错");
        return Result.success();
    }

    // 确定收货接口
    @PutMapping("/receipt/{id}")
    public Result<?> receipt(@PathVariable String id){
        // 创建当前时间
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String now = format.format(new Date());
        // 更新订单状态和结束时间
        if (orderMapper.updateReceiptState(id,now) <= 0)
            throw new ServiceException("400","，确认收货出错");
        return Result.success();
    }

    // 根据订单编号删除订单接口
    @DeleteMapping("/delete/{orderNo}")
    public Result<?> deleteByOrderNo(@PathVariable String orderNo){
        if (orderMapper.deleteByOrderNo(orderNo) <= 0)
            throw new ServiceException("400","，删除订单出错");
        return Result.success();
    }

    /**
     * 获取驾驶舱所需要的图表数据
     * @return 图表数据
     */
    @GetMapping("/charts")
    public Result<?> charts() {
        // 包装折线图的数据
        LambdaQueryWrapper<Order> wrapper = Wrappers.<Order>lambdaQuery().isNotNull(Order::getPaymentTime);
        List<Order> orders = orderMapper.selectList(wrapper);
        // 获取下单的日期，并对日期排序
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        List<String> dateList = orders.stream().map(order -> (format.format(order.getPaymentTime())))
                .distinct().sorted(Comparator.naturalOrder()).collect(Collectors.toList());
        List<Dict> lineList = new ArrayList<>();
        // 获取每天下单的金额和
        for (String date : dateList) {
            // 从 orders 里面找到这一天的订单
            BigDecimal sum = orders.stream().filter(order -> format.format(order.getPaymentTime()).equals(date))
                    .map(order -> order.getPayPrice().multiply(new BigDecimal(order.getCount())))
                    .reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
            Dict dict = Dict.create().set("date",date).set("value",sum);
            lineList.add(dict);
        }

        List<Dict> barList = new ArrayList<>();
        // 包装柱状图的数据，每天的订单量
        for (String date : dateList) {
            Integer numbers = orders.stream().filter(order -> format.format(order.getPaymentTime()).equals(date))
                    // 把每个订单映射成 1
                    .map(order -> 1).reduce(Math::addExact).orElse(0);
            Dict dict = Dict.create().set("date",date).set("count",numbers);
            barList.add(dict);
        }

        // todo 饼图的数据根据商品 id 来分类

        Dict res = Dict.create().set("lineData", lineList).set("barData", barList);
        // 包装柱状图数据
        return Result.success(res);
    }
}
