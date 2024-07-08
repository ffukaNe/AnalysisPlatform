package com.xia.springboot.exception;

import com.xia.springboot.common.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Author: Xia
 * @Date: 2024/4/2  15:36
 */

    // Controller的增强，可以把这个类应用到所有的 Controller，然后再类的内部通过 @ExceptionHandler 来处理不同的异常
    @ControllerAdvice
    public class GlobalException {
    // 处理 ServiceException 的异常
    @ExceptionHandler(ServiceException.class)
    @ResponseBody
    public Result<?> serviceException(ServiceException e){
        return Result.error(e.getCode(),e.getMessage());
    }
}
