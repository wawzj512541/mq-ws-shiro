/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.config;

import com.example.shiro.common.shiro.MyWebSessionManager;
import com.example.shiro.common.shiro.ShiroLoginFilter;
import com.example.shiro.common.shiro.UuidSessionIdGenerator;
import com.example.shiro.sys.shiro.SysUserRealm;
import com.example.shiro.web.realm.MyCustomModularRealmAuthenticator;
import com.example.shiro.web.realm.WebUserPhoneRealm;
import com.example.shiro.web.realm.WebUserRealm;
import org.apache.shiro.authc.AbstractAuthenticator;
import org.apache.shiro.authc.pam.AtLeastOneSuccessfulStrategy;
import org.apache.shiro.authc.pam.ModularRealmAuthenticator;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.crazycake.shiro.RedisCacheManager;
import org.crazycake.shiro.RedisManager;
import org.crazycake.shiro.RedisSessionDAO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Filter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Shiro的配置文件
 *
 * @author Mark sunlightcs@gmail.com
 */
@Configuration
public class ShiroConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private Integer port;

    @Value("${spring.redis.password}")
    private String password;

    @Value("${spring.redis.timeout}")
    private Integer timeout;

    @Value("${system.expire}")
    private Integer expire;

    private String sessionPrefix;   //session前缀,用于存储用户会话信息
    private String cachePrefix;     //cache前缀,用于存储用户权限
    private String cookidName;      //cookID名称,防止前后端JSessionId冲突

    @Value("${system.manager}")
    private void setShiroPrefix(Boolean manager) {
        if (manager) {
            sessionPrefix = "shiro:sys:session:";
            cachePrefix = "shiro:sys:cache:";
            cookidName = "ZHONGKE";
        } else {
            sessionPrefix = "shiro:web:session:";
            cachePrefix = "shiro:web:cache:";
            cookidName = "ZHONGKEHUAIBEI";
        }
    }

    /**
     * 配置shiro redisManager
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisManager redisManager() {
        RedisManager redisManager = new RedisManager();
        redisManager.setHost(host + ":" + port);
        redisManager.setTimeout(timeout);
        return redisManager;
    }

    /**
     * cacheManager 缓存 redis实现
     * 使用的是shiro-redis开源插件
     *
     * @return
     */
    public RedisCacheManager cacheManager() {
        RedisCacheManager redisCacheManager = new RedisCacheManager();
        redisCacheManager.setPrincipalIdFieldName("userId");
        redisCacheManager.setRedisManager(redisManager());
        redisCacheManager.setExpire(expire);   //时间单位秒,默认值30分钟
        redisCacheManager.setKeyPrefix(cachePrefix);
        return redisCacheManager;
    }

    /**
     * RedisSessionDAO shiro sessionDao层的实现 通过redis
     * 使用的是shiro-redis开源插件
     */
    @Bean("redisSessionDAO")
    public RedisSessionDAO redisSessionDAO() {
        RedisSessionDAO redisSessionDAO = new RedisSessionDAO();
        redisSessionDAO.setSessionIdGenerator(new UuidSessionIdGenerator());
        redisSessionDAO.setRedisManager(redisManager());
        redisSessionDAO.setKeyPrefix(sessionPrefix);
        return redisSessionDAO;
    }

    /**
     * 单机环境，session交给shiro管理
     */
    @Bean
    public DefaultWebSessionManager sessionManager(RedisSessionDAO redisSessionDAO) {
        MyWebSessionManager sessionManager = new MyWebSessionManager();
        // 定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationSchedulerEnabled(true);
        sessionManager.setSessionDAO(redisSessionDAO);
        sessionManager.setGlobalSessionTimeout(expire * 1000); //时间单位毫秒,默认值30分钟
        sessionManager.setSessionIdCookie(sessionIdCookie());
        return sessionManager;
    }


    @Bean
    @ConditionalOnProperty(prefix = "system", name = "manager", havingValue = "true")
    public SecurityManager securityManager(SysUserRealm userRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(userRealm);
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    @Bean
    @ConditionalOnProperty(prefix = "system", name = "manager", havingValue = "false")
    public SecurityManager webSecurityManager(WebUserRealm userRealm, WebUserPhoneRealm webUserPhoneRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm);
        realms.add(webUserPhoneRealm);
        securityManager.setRealms(realms);
        // 自定义缓存实现 使用redis
        securityManager.setCacheManager(cacheManager());
        securityManager.setSessionManager(sessionManager);
        securityManager.setAuthenticator(abstractAuthenticator(userRealm, webUserPhoneRealm));
        return securityManager;
    }

    /**
     * 认证器 把我们的自定义验证加入到认证器中
     */
    @Bean
    public AbstractAuthenticator abstractAuthenticator(WebUserRealm userRealm, WebUserPhoneRealm webUserPhoneRealm) {
        // 自定义模块化认证器，用于解决多realm抛出异常问题
        //开始没用自定义异常问题，发现不管是账号密码错误还是什么错误
        //shiro只会抛出一个AuthenticationException异常
        ModularRealmAuthenticator authenticator = new MyCustomModularRealmAuthenticator();
        // 认证策略：AtLeastOneSuccessfulStrategy(默认)，AllSuccessfulStrategy，FirstSuccessfulStrategy
        authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
        // 加入realms
        List<Realm> realms = new ArrayList<>();
        realms.add(userRealm);
        realms.add(webUserPhoneRealm);
        authenticator.setRealms(realms);
        return authenticator;
    }

    @Bean("shiroFilter")
    @ConditionalOnProperty(prefix = "system", name = "manager", havingValue = "true")
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);
        Map<String, Filter> filters = shiroFilter.getFilters();
        filters.put("authc", new ShiroLoginFilter());

        shiroFilter.setLoginUrl("/#/login");
//        shiroFilter.setUnauthorizedUrl("/#/home");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/swagger/**", "anon");
        filterMap.put("/v2/api-docs", "anon");
        filterMap.put("/swagger-ui.html", "anon");
        filterMap.put("/webjars/**", "anon");
        filterMap.put("/swagger-resources/**", "anon");

        filterMap.put("/static/**", "anon");
        filterMap.put("/*/login", "anon");
        filterMap.put("/index.html", "anon");
        filterMap.put("/img/**", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/captcha.jpg", "anon");
        filterMap.put("/logout", "anon");
        filterMap.put("/**/*", "authc");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean("shiroFilter")
    @ConditionalOnProperty(prefix = "system", name = "manager", havingValue = "false")
    public ShiroFilterFactoryBean webShiroFilter(SecurityManager securityManager) {
        ShiroFilterFactoryBean shiroFilter = new ShiroFilterFactoryBean();
        shiroFilter.setSecurityManager(securityManager);

        Map<String, Filter> filters = shiroFilter.getFilters();
        filters.put("authc", new ShiroLoginFilter());

        shiroFilter.setLoginUrl("/#/login");
        shiroFilter.setUnauthorizedUrl("/#/home");

        Map<String, String> filterMap = new LinkedHashMap<>();
        filterMap.put("/#/home", "anon");
        filterMap.put("/#/login", "anon");
        filterMap.put("/static/**", "anon");
        filterMap.put("/template/**", "anon");
        filterMap.put("/index.html", "anon");
        filterMap.put("/favicon.ico", "anon");
        filterMap.put("/**/*", "anon");
        shiroFilter.setFilterChainDefinitionMap(filterMap);
        return shiroFilter;
    }

    @Bean("lifecycleBeanPostProcessor")
    public static LifecycleBeanPostProcessor lifecycleBeanPostProcessor() {
        return new LifecycleBeanPostProcessor();
    }

    @Bean
    public AuthorizationAttributeSourceAdvisor authorizationAttributeSourceAdvisor(SecurityManager securityManager) {
        AuthorizationAttributeSourceAdvisor advisor = new AuthorizationAttributeSourceAdvisor();
        advisor.setSecurityManager(securityManager);
        return advisor;
    }

    //设置cook的名字
    @Bean(name = "sessionIdCookie")
    public Cookie sessionIdCookie() {
        Cookie cookie = new SimpleCookie(cookidName);
        cookie.setHttpOnly(true);
        return cookie;
    }
}
