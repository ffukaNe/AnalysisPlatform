package com.xia.springboot.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import com.xia.springboot.common.Result;
import com.xia.springboot.entity.UserAdvice;
import com.xia.springboot.mapper.UserAdviceMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.sql.Timestamp;

/**
 * @Author: Xia
 * @Date: 2023/11/17  16:36
 */
@RestController
@RequestMapping("/customerServ")
public class CustomerService {
    @Resource
    private UserAdviceMapper userAdviceMapper;
    // 用户建议收集
    @PostMapping
    public Result<?> addAdvice(@RequestBody UserAdvice userAdvice){
        // 给建议加上日期
        userAdvice.setCreateTime(new Timestamp(System.currentTimeMillis()));
        int row = userAdviceMapper.insert(userAdvice);
        if (row == 1)
            return Result.success();
        else
            return Result.error("500","建议插入数据库时发生错误");
    }

    @GetMapping
    public Result<?> getAdvice(@RequestParam(defaultValue = "1") Integer pageNum,
                               @RequestParam(defaultValue = "10") Integer pageSize,
                               @RequestParam(defaultValue = "") String search){
        // 根据页码和页大小获取建议
        Page<UserAdvice> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<UserAdvice> wrapper = Wrappers.lambdaQuery();
        // 添加搜索词
        if (StrUtil.isNotEmpty(search))
            wrapper.like(UserAdvice::getUsername,search);
        Page<UserAdvice> result = userAdviceMapper.selectPage(page, wrapper);
        return Result.success(result);
    }

    @DeleteMapping
    public Result<?> deleteAdvice(@RequestParam Integer id){
        int row = userAdviceMapper.deleteById(id);
        if (row == 1)
            return Result.success();
        else
            return Result.error("400","未删除成功");
    }
}
