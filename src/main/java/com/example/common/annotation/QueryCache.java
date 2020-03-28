package com.example.common.annotation;


import com.example.common.CacheNameSpace;

import java.lang.annotation.*;

/**
 * Created by mazhenhua on 2017/5/3.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
@Documented
public @interface QueryCache {
    CacheNameSpace nameSpace();
    int expire() default 30;
}