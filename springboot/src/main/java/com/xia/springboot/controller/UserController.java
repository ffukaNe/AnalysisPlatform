package com.xia.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.springboot.common.AuthAccess;
import com.xia.springboot.common.Result;
import com.xia.springboot.controller.dto.UserDto;
import com.xia.springboot.entity.User;
import com.xia.springboot.exception.ServiceException;
import com.xia.springboot.mapper.UserMapper;
import com.xia.springboot.utils.TokenUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 这里是为了简化开发流程，因为实际开发中其实应该再写一个Service层来调用Mapper层，在Service层里面写逻辑，在Controller层返回信息
 * 这里直接Controller层调用mapper层，并且对数据进行处理，然后返回结果
 *
 * 还有这里的@RestController因为是前后端分离项目，所以后端只用返回json数据就可以了，或者说对象数据。
 * 所以不能用@Controller，它会返回一个视图
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    UserMapper userMapper;

    //    注册方法
    //    这里可以用User进行接受，因为虽然数据不全，但是是User里面的数据
    @AuthAccess(message = "注册页面放行")
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user){
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername,user.getUsername()));
        if (res == null){
            userMapper.insert(user);
            return Result.success();
        }
        return Result.error("-1","用户名已经存在，请重新输入");
    }

    //    登录接口 这里可以用User进行接受，因为虽然数据不全，但是是User里面的数据
    @PostMapping("/login")
    public Result<?> login(@RequestBody User user){
        /*
        这里使用这样的where查询太慢了，不符合数据库的索引结构
        查找一个user 用户名要相等，密码也要相等
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery()
                .eq(User::getUsername,user.getUsername())
                .eq(User::getPassword,user.getPassword()));
         */
        // 因为我们用户名是唯一的，所以给用户名加一个索引，然后通过用户名查就好了，这样效率更快
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res == null)
            throw new ServiceException("404","用户名不存在");
        if (!res.getPassword().equals(user.getPassword()))
            throw new ServiceException("404","密码错误");
        // 生成 token，用户id保存到token里面，然后密码用于加密
        String token = TokenUtils.genToken(res.getId().toString());
        // 生成 UserDto
        UserDto userDto = new UserDto(res,token);
        return Result.success(userDto);
    }

    @PostMapping("/manageLogin")
    public Result<?> manageLogin(@RequestBody User user){
        // 因为我们用户名是唯一的，所以给用户名加一个索引，然后通过用户名查就好了，这样效率更快
        User res = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if (res == null)
            throw new ServiceException("404","用户名不存在");
        if (!res.getPassword().equals(user.getPassword()))
            throw new ServiceException("404","密码错误");
        if (res.getRole() != 1)
            throw new ServiceException("404","当前用户权限不足");
        // 生成 token，用户id保存到token里面，然后密码用于加密
        String token = TokenUtils.genToken(res.getId().toString());
        // 生成 UserDto
        UserDto userDto = new UserDto(res,token);
        return Result.success(userDto);
    }

    @PostMapping
    //    新增方法
    //    @CrossOrigin   //后端设置跨域，没试过，不知道行不行感觉其实不能在后端设置跨域，不然接口就暴露了
    public Result<?> save(@RequestBody User user){
        userMapper.insert(user);
        return Result.success();
    }


    @PutMapping
    //    更新信息
    public Result<?> update(@RequestBody User user){
        userMapper.updateById(user);
        return Result.success();
    }

    @DeleteMapping
    //    删除信息
    public Result<?> delete(@RequestParam Long id){
        userMapper.deleteById(id);
        return Result.success();
    }

    /**
     * 前后端的字段名不一定要一样，可以在前端传入数据的时候设置一下就可以了，只是比较麻烦，所以一般将实体的设置成一样
     * 请求分页数据
     * @param pageNum 当前页码
     * @param pageSize 每一页显示多少个数据
     * @param search 模糊搜索的关键词
     * @return 返回一个封装好的res
     */
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {
        //新建分页对象
        Page<User> page = new Page<>(pageNum, pageSize);
        //新建模糊查询对象,这里有个注意事项，你模糊项查询的对应项不能为null，为null就查不出来
        //LambdaQueryWrapper<User> queryWrapper = Wrappers.<User>lambdaQuery().like(User::getUsername, search);
        LambdaQueryWrapper<User> queryWrapper = Wrappers.lambdaQuery();
        if (!search.equals(""))
            queryWrapper.like(User::getUsername,search);
        Page<User> userPage = userMapper.selectPage(page, queryWrapper);
        return Result.success(userPage);
    }
}