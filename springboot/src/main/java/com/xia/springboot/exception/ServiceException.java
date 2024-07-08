package com.xia.springboot.exception;

import lombok.Getter;

/**
 * @Author: Xia
 * @Date: 2024/4/2  15:34
 */

@Getter
public class ServiceException extends RuntimeException {
    private final String code;

    public ServiceException(String code, String msg) {
        super(msg);
        this.code = code;
    }
}
