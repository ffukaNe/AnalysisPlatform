package com.xia.springboot.service;

import com.xia.springboot.common.Result;
import com.xia.springboot.entity.User;

public interface UserService {
    Result<?> register(User user);

    Result<?> login(User user);

    Result<?> save(User user);

    Result<?> update(User user);

    Result<?> delete(Long id);

    Result<?> findPage(Integer pageNum,Integer pageSize,String search);
}
