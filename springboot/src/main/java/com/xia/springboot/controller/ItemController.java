package com.xia.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.springboot.common.AuthAccess;
import com.xia.springboot.common.Result;
import com.xia.springboot.entity.Item;
import com.xia.springboot.entity.ItemDetail;
import com.xia.springboot.mapper.ItemDetailMapper;
import com.xia.springboot.mapper.ItemMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 这里是为了简化开发流程，因为实际开发中其实应该再写一个Service层来调用Mapper层，在Service层里面写逻辑，在Controller层返回信息
 * 这里直接Controller层调用mapper层，并且对数据进行处理，然后返回结果
 *
 * 还有这里的@RestController因为是前后端分离项目，所以后端只用返回json数据就可以了，或者说对象数据。
 * 所以不能用@Controller，它会返回一个视图
 */
@RestController
@RequestMapping("/item")
public class ItemController {

    @Resource
    ItemMapper itemMapper;

    @Resource
    ItemDetailMapper itemDetailMapper;

    // 写一个查询所有的接口
    @GetMapping("/getAll")
    public Result<?> getAll(@RequestParam(defaultValue = "") String search){
        LambdaQueryWrapper<Item> wrapper = Wrappers.lambdaQuery();
        wrapper.orderByDesc(Item::getCreated);
        if (!Strings.isBlank(search))
            wrapper.like(Item::getSellPoint,search);
        List<Item> items = itemMapper.selectList(wrapper);
        return Result.success(items);
    }


    //    新增方法，这里新增了商品之后要对应新增一个空白的商品详情
    //    @CrossOrigin
    //    后端设置跨域，没试过，不知道行不行，感觉其实不能在后端设置跨域，不然接口就暴露了
    @PostMapping
    public Result<?> save(@RequestBody Item item){
        Date currDate = new Date();
        item.setCreated(currDate);
        itemMapper.insert(item);
        // 新增空白的商品详情
        ItemDetail itemDetail = new ItemDetail();
        itemDetail.setItemId(item.getId());
        itemDetail.setItemDetail("<p></p>");
        itemDetail.setCreateTime(currDate);
        itemDetailMapper.insert(itemDetail);
        return Result.success();
    }


    @PutMapping
    //    更新信息
    public Result<?> update(@RequestBody Item item){
        item.setUpdated(new Date());
        itemMapper.updateById(item);
        return Result.success();
    }

    @DeleteMapping
    //    删除信息
    public Result<?> delete(@RequestParam Long id){
        itemMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 前后端的字段名不一定要一样，可以在前端传入数据的时候设置一下就可以了，只是比较麻烦，所以一般将实体的设置成一样
     * 请求分页数据,请求全部商品
     * @param pageNum 当前页码
     * @param pageSize 每一页显示多少个数据
     * @param search 模糊搜索的关键词
     * @return 返回一个封装好的res
     */
    @AuthAccess
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        //新建分页对象
        Page<Item> page = new Page<>(pageNum, pageSize);
        //新建模糊查询对象,这里有个注意事项，你模糊项查询的对应项不能为null，为null就查不出来
        //LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().like(User::getUsername, search);
        LambdaQueryWrapper<Item> queryWrapper = Wrappers.lambdaQuery();
        // 根据更新日期降序排列
        queryWrapper.orderByDesc(Item::getCreated);
        if (!search.equals(""))
            queryWrapper.like(Item::getSellPoint,search);
        Page<Item> itemPage = itemMapper.selectPage(page, queryWrapper);
        return Result.success(itemPage);
    }

    @AuthAccess
    @GetMapping("/one")
    public Result<?> findById(@RequestParam Integer id){
        //找到ID相同的商品
        Item item = itemMapper.selectOne(Wrappers.<Item>lambdaQuery().eq(Item::getId, id));
        return Result.success(item);
    }

    /**
     * 根据 cid 来查询不同分类的商品
     * @param cid 商品分类
     */
    @AuthAccess
    @GetMapping("/category")
    public Result<?> findByCid(@RequestParam Integer cid){
        LambdaQueryWrapper<Item> wrapper = Wrappers.<Item>lambdaQuery().eq(Item::getCid, cid);
        List<Item> items = itemMapper.selectList(wrapper);
        return Result.success(items);
    }
}