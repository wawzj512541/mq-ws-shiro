package com.example.common.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 避免用户重复提交
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-04-17 17:48:55
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AvoidRepeatableCommit {
    /**
     * 指定时间内不可重复提交,单位秒
     */
    long timeout()  default 3 ;

    String type() default "";

}
