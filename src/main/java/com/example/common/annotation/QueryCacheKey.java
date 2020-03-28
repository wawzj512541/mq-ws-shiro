package com.example.common.annotation;

import java.lang.annotation.*;

/**
 * Created by mazhenhua on 2017/5/3.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER})
@Documented
public @interface QueryCacheKey {

}
