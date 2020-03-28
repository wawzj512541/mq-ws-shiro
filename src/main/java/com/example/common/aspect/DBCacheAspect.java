package com.example.common.aspect;


import com.example.common.CacheNameSpace;
import com.example.common.annotation.QueryCache;
import com.example.common.annotation.QueryCacheKey;
import com.example.common.utils.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.MethodParameter;
import org.springframework.core.annotation.SynthesizingMethodParameter;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * Created by mazhenhua on 2017/5/3.
 */
@Aspect
@Service
@Slf4j
public class DBCacheAspect {


    @Resource
    private RedisUtil redisUtil;

    /**
     * 定义拦截规则：拦截所有@QueryCache注解的方法。
     */
    @Pointcut("@annotation(com.example.common.annotation.QueryCache)")
    public void queryCachePointcut() {
    }

    /**
     * 拦截器具体实现
     *
     * @param pjp   节点
     * @return  object
     * @throws Throwable    异常抛出
     */
    @Around("queryCachePointcut()")
    public Object Interceptor(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); //获取被拦截的方法
        CacheNameSpace cacheType = method.getAnnotation(QueryCache.class).nameSpace();
        int expire = method.getAnnotation(QueryCache.class).expire();
        StringBuilder key = new StringBuilder(cacheType.name());
        int i = 0;
        // 循环所有的参数
        for (Object value : pjp.getArgs()) {
            MethodParameter methodParam = new SynthesizingMethodParameter(method, i);
            Annotation[] paramAnns = methodParam.getParameterAnnotations();

            // 循环参数上所有的注解
            for (Annotation paramAnn : paramAnns) {
                if (paramAnn instanceof QueryCacheKey) { //
                    key.append("_").append(value);   // 取到QueryCacheKey的标识参数的值
                }
            }
            i++;
        }

        // 获取不到key值，抛异常
        if (StringUtils.isBlank(key.toString())) throw new Exception("缓存key值不存在");

        boolean hasKey = redisUtil.hasKey(key.toString());
        Object object;
        if (hasKey) {
            // 缓存中获取到数据，直接返回。
            object = redisUtil.get(key.toString());
        }else{
            // 缓存中没有数据，调用原始方法查询数据库
            object = pjp.proceed();
        }
        redisUtil.set(key.toString(),object,expire*60);
        return object;
    }


}

