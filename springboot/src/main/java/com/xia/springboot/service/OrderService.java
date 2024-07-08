package com.xia.springboot.service;

import com.xia.springboot.common.Result;
import com.xia.springboot.entity.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderService {
    Result<?> save(Order order);

    Result<?> update(Order order);

    Result<?> delete(Long id);

    Result<?> getById(Long id);

    Result<?> buy(Long itemId, Integer userId);

    Result<?> buyCart(List<Order> orders, BigDecimal totalPrice);

    Result<?> addToCart(Long itemId, Integer userId);

    Result<?> findPage(Integer pageNum, Integer pageSize,String search);

    Result<?> findUserOrder(Integer pageNum, Integer pageSize,String search,boolean unpaid);
}
