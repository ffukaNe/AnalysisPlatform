package com.xia.springboot.service;

import com.xia.springboot.common.Result;
import com.xia.springboot.entity.ItemDetail;

public interface ItemDetailService {
    Result<?> save(ItemDetail item);

    Result<?> update(ItemDetail item);

    Result<?> delete(Long id);

    Result<?> findById(Long id);

    Result<?> getAll(Integer pageNum, Integer pageSize,String search);
}
