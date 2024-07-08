package com.xia.springboot.common;

import java.lang.annotation.*;

/**
 * @Author: Xia
 * @Date: 2024/4/3  18:25
 */

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AuthAccess {
    String message() default "";
}
