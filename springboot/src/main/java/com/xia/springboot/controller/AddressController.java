package com.xia.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.springboot.common.Result;
import com.xia.springboot.entity.Address;
import com.xia.springboot.mapper.AddressMapper;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/address")
public class AddressController {

    @Resource
    private AddressMapper addressMapper;

    // 通过 userid 来查询所有地址
    @GetMapping
    public Result<?> getPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             @RequestParam Integer userId){
        Page<Address> page = new Page<>(pageNum,pageSize);
        LambdaQueryWrapper<Address> wrapper = Wrappers.lambdaQuery();
        // 查询当前的 userId
        wrapper.eq(Address::getUserId,userId);
        page = addressMapper.selectPage(page,wrapper);
        return Result.success(page);
    }

    // 新增地址
    @PostMapping
    public Result<?> add(@RequestBody Address address){
        if (addressMapper.insert(address) == 0)
            return Result.error("400","新增地址插入失败");
        else
            return Result.success();
    }

    // 删除地址
    @DeleteMapping
    public Result<?> delete(@RequestParam Integer id){
        if (addressMapper.deleteById(id) == 0)
            return Result.error("400","删除地址失败");
        else
            return Result.success();
    }

    @DeleteMapping("{id}")
    public Result<?> deleteByPath(@PathVariable Integer id){
        if (addressMapper.deleteById(id) == 0)
            return Result.error("400","删除地址失败");
        else
            return Result.success();
    }

    // 更新地址
    @PutMapping
    public Result<?> update(@RequestBody Address address){
        if (addressMapper.updateById(address) == 0)
            return Result.error("400","更新地址失败");
        else
            return Result.success();
    }

    // 获取到单个地址
    @GetMapping("/one")
    public Result<?> getOne(@RequestParam Integer id){
        Address address = addressMapper.selectById(id);
        if (address != null)
            return  Result.success(address);
        return Result.error("401","请求的地址无效");
    }
}
