/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.shiro.common.utils;

import com.example.shiro.common.RRException;
import com.example.shiro.sys.entity.SysUserEntity;
import com.example.shiro.web.entity.WebUserEntity;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.Authenticator;
import org.apache.shiro.authc.LogoutAware;
import org.apache.shiro.crypto.hash.SimpleHash;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.SimplePrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.subject.support.DefaultSubjectContext;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;

/**
 * Shiro工具类
 *
 * @author Mark sunlightcs@gmail.com
 */
public class ShiroUtils {

    /**
     * 加密算法
     */
    public final static String hashAlgorithmName = "SHA-256";
    /**
     * 循环次数
     */
    public final static int hashIterations = 16;

    public static String sha256(String password, String salt) {
        return new SimpleHash(hashAlgorithmName, password, salt, hashIterations).toString();
    }

    public static Session getSession() {
        return SecurityUtils.getSubject().getSession();
    }

    public static Subject getSubject() {
        return SecurityUtils.getSubject();
    }

    public static SysUserEntity getUserEntity() {
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getUserId() {
        return getUserEntity().getUserId();
    }

    public static WebUserEntity getWebUser(){
        return (WebUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

    public static Long getWebUserId(){
        return getWebUser().getUserId();
    }

    public static void setSessionAttribute(Object key, Object value) {
        getSession().setAttribute(key, value);
    }

    public static Object getSessionAttribute(Object key) {
        return getSession().getAttribute(key);
    }

    public static boolean isLogin() {
        return SecurityUtils.getSubject().getPrincipal() != null;
    }

    public static void logout() {
        SecurityUtils.getSubject().logout();
    }

    /**
     * 删除用户缓存信息
     *
     * @param userId          用户id
     */
    public static void removeCache(Long userId) {
        Session session = getSession();
        if (session == null) {
            return;
        }
        Object attribute = session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY);
        if (attribute == null) {
            return;
        }
        SysUserEntity user = (SysUserEntity) ((SimplePrincipalCollection) attribute).getPrimaryPrincipal();
        if (!userId.equals(user.getUserId())) {
            return;
        }
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) SecurityUtils.getSecurityManager();
        Authenticator authc = securityManager.getAuthenticator();
        //删除cache，在访问受限接口时会重新授权
        ((LogoutAware) authc).onLogout((SimplePrincipalCollection) attribute);
    }

    public static String getKaptcha(String key) {
        Object kaptcha = getSessionAttribute(key);
        if (kaptcha == null) {
            throw new RRException("验证码已失效");
        }
        getSession().removeAttribute(key);
        return kaptcha.toString();
    }

    public static String getUserName() {
        if (isLogin()) {
            String username = "";
            Object obj = SecurityUtils.getSubject().getPrincipal();
                String className1 = obj.getClass().getName();
                if(className1.indexOf("SysUserEntity")>0){
                    username = ((SysUserEntity)obj).getUsername();
                }else{
                    username = ((WebUserEntity)obj).getUsername();
                }
            return username;
        } else {
            return null;
        }
    }

    public static Long getUserId1() {
        if (isLogin()) {
            Long userId = 0L;
            Object obj = SecurityUtils.getSubject().getPrincipal();
            String className1 = obj.getClass().getName();
            if(className1.indexOf("SysUserEntity")>0){
                userId = ((SysUserEntity)obj).getUserId();
            }else{
                userId = ((WebUserEntity)obj).getUserId();
            }
            return userId;
        } else {
            return null;
        }
    }
}
