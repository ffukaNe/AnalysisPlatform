package com.xia.springboot.service;

import com.xia.springboot.common.Result;
import com.xia.springboot.entity.Item;

public interface ItemService {
    Result<?> save(Item item);
    Result<?> update(Item item);
    Result<?> delete(Long id);
    Result<?> findPage(Integer pageNum,Integer pageSize,String search);
    Result<?> findById(Integer id);
}
