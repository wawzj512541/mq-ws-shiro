/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.shiro.sys.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.common.utils.ShiroUtils;
import com.example.shiro.sys.dao.SysMenuDao;
import com.example.shiro.sys.dao.SysUserDao;
import com.example.shiro.sys.entity.SysMenuEntity;
import com.example.shiro.sys.entity.SysUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class SysUserRealm extends AuthorizingRealm {
    @Autowired
    private SysUserDao sysUserDao;
    @Autowired
    private SysMenuDao sysMenuDao;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        SysUserEntity user = (SysUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<SysMenuEntity> menuList = sysMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (SysMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = sysUserDao.queryAllPerms(userId);
        }

        //用户权限列表
        Set<String> permsSet = new HashSet<>();
        for (String perms : permsList) {
            if (StringUtils.isBlank(perms)) {
                continue;
            }
            permsSet.addAll(Arrays.asList(perms.trim().split(",")));
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(permsSet);
        return info;
    }

    /**
     * 认证(登录时调用)
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(
            AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authcToken;
        String username = token.getUsername();
        //查询用户信息
        SysUserEntity user = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", username));
//        SysUserEntity use = sysUserDao.selectUserByUsername(token.getUsername());
        //账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }

        //账号锁定
        if (user.getStatus() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }

        //处理session
//        SessionsSecurityManager securityManager = (SessionsSecurityManager) SecurityUtils.getSecurityManager();
//        DefaultSessionManager sessionManager = (DefaultSessionManager) securityManager.getSessionManager();
//        Collection<Session> sessions = sessionManager.getSessionDAO().getActiveSessions();//获取当前已登录的用户session列表
//        for (Session session : sessions) {
//            //清除该用户以前登录时保存的session
//            //如果和当前session是同一个session，则不剔除
//            if (SecurityUtils.getSubject().getSession().getId().equals(session.getId()))
//                break;
//            SimplePrincipalCollection coll = (SimplePrincipalCollection)(session.getAttribute(DefaultSubjectContext.PRINCIPALS_SESSION_KEY));
//            SysUserEntity entity = (SysUserEntity) coll.getPrimaryPrincipal();
//            String sessionName = entity.getUsername();
//            if(username.equals(sessionName)){
//                System.out.println(username + "已登录，剔除中...");
//                sessionManager.getSessionDAO().delete(session);
//            }
//        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
        return info;
    }

    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtils.hashAlgorithmName);
        shaCredentialsMatcher.setHashIterations(ShiroUtils.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }
}
