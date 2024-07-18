package com.xia.springboot.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xia.springboot.common.Result;
import com.xia.springboot.entity.RefundOrder;
import com.xia.springboot.mapper.RefundOrderMapper;
import org.apache.logging.log4j.util.Strings;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;


@RestController
@RequestMapping("/refund")
public class RefundOrderController {
    @Resource
    private RefundOrderMapper refundOrderMapper;

    // 查询所有退款订单
    @GetMapping
    public Result<?> getAll(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String search){
        // 新建分页插件
        Page<RefundOrder> page = new Page<>(pageNum,pageSize);
        // 新建模糊 wrapper
        LambdaQueryWrapper<RefundOrder> wrapper = Wrappers.lambdaQuery();
        // 排序
        wrapper.orderByDesc(RefundOrder::getId);
        if (!Strings.isEmpty(search))
            wrapper.like(RefundOrder::getUsername,search);
        // 开始查询
        Page<RefundOrder> refundOrders = refundOrderMapper.selectPage(page, wrapper);
        return Result.success(refundOrders);
    }

    @DeleteMapping("/{id}")
    public Result<?> deleteOne(@PathVariable("id") Integer id){
        refundOrderMapper.deleteById(id);
        return Result.success();
    }
}
