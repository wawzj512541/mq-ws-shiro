package com.example.common.aspect;

import com.example.common.annotation.AvoidRepeatableCommit;
import com.example.common.utils.RedisUtil;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.common.utils.IPUtils;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.util.UUID;

/**
 * 重复提交aop
 *
 * @data: 2019-03-26 16:39
 **/
@Aspect
@Component
@Slf4j
public class AvoidRepeatableCommitAspect {

    @Autowired
    private RedisUtil redisUtil;

    /**
     * @param point
     */
    @Around("@annotation(com.example.common.annotation.AvoidRepeatableCommit)")
    public Object around(ProceedingJoinPoint point) throws Throwable {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
        String ip = IPUtils.getIpAddr(request);
        //获取注解
        MethodSignature signature = (MethodSignature) point.getSignature();
        Method method = signature.getMethod();
        //目标类、方法
        String className = method.getDeclaringClass().getName();
        String name = method.getName();
        String ipKey = String.format("%s#%s", className, name);
        int hashCode = Math.abs(ipKey.hashCode());

        AvoidRepeatableCommit avoidRepeatableCommit = method.getAnnotation(AvoidRepeatableCommit.class);
        long timeout = avoidRepeatableCommit.timeout();
        String type = avoidRepeatableCommit.type();

        String key = Constant.REDIS_ROOT + String.format("%s_%s_%d", type, ip, hashCode);

        log.info("ipKey={},hashCode={},key={}", ipKey, hashCode, key);

        if (timeout < 0) {
            timeout = 3;
        }
        if (redisUtil.hasKey(key)) {
            return APIResponse.returnFail("请勿重复提交!");
        }
        redisUtil.set(key, UUID.randomUUID().toString(), timeout);
        //执行方法
        Object object = point.proceed();

        return object;
    }

}
