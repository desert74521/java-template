package com.test.platform.aop.annotation;

import java.lang.annotation.*;

/**
 * @author hui kang
 * @version 1.0
 * @date 2019/10/11 9:50
 */
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Auth {}
