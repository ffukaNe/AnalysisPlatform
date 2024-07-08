package com.xia.springboot.service.Impl;

import com.xia.springboot.common.Result;
import com.xia.springboot.entity.Order;
import com.xia.springboot.service.OrderService;

import java.math.BigDecimal;
import java.util.List;

public class OrderServiceImpl implements OrderService {
    @Override
    public Result<?> save(Order order) {
        return null;
    }

    @Override
    public Result<?> update(Order order) {
        return null;
    }

    @Override
    public Result<?> delete(Long id) {
        return null;
    }

    @Override
    public Result<?> getById(Long id) {
        return null;
    }

    @Override
    public Result<?> buy(Long itemId, Integer userId) {
        return null;
    }

    @Override
    public Result<?> buyCart(List<Order> orders, BigDecimal totalPrice) {
        return null;
    }

    @Override
    public Result<?> addToCart(Long itemId, Integer userId) {
        return null;
    }

    @Override
    public Result<?> findPage(Integer pageNum, Integer pageSize, String search) {
        return null;
    }

    @Override
    public Result<?> findUserOrder(Integer pageNum, Integer pageSize, String search, boolean unpaid) {
        return null;
    }
}
