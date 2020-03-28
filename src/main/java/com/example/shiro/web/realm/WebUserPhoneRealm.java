/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.shiro.web.realm;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.web.dao.WebMenuDao;
import com.example.shiro.web.dao.WebUserDao;
import com.example.shiro.web.entity.WebMenuEntity;
import com.example.shiro.web.entity.WebUserEntity;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 认证
 *
 * @author Mark sunlightcs@gmail.com
 */
@Component
public class WebUserPhoneRealm extends AuthorizingRealm {
    @Autowired
    private WebUserDao webUserDao;
    @Autowired
    private WebMenuDao webMenuDao;

    /**
     * 授权(验证权限时调用)
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        WebUserEntity user = (WebUserEntity) principals.getPrimaryPrincipal();
        Long userId = user.getUserId();

        List<String> permsList;

        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            List<WebMenuEntity> menuList = webMenuDao.selectList(null);
            permsList = new ArrayList<>(menuList.size());
            for (WebMenuEntity menu : menuList) {
                permsList.add(menu.getPerms());
            }
        } else {
            permsList = webUserDao.queryAllPerms(userId);
            if(permsList==null||permsList.size()<=0){

            }
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
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authcToken) throws AuthenticationException {
        UsernamePhoneCodeToken token = null;
        // 如果是PhoneToken，则强转，获取phone；否则不处理。
        if(authcToken instanceof UsernamePhoneCodeToken){
            token = (UsernamePhoneCodeToken) authcToken;
        }else{
            return null;
        }
        //查询用户信息
        WebUserEntity user = webUserDao.selectOne(new QueryWrapper<WebUserEntity>().eq("phone_number", token.getPhoneNumber()));
        //账号不存在
        if (user == null) {
            throw new UnknownAccountException("账号或密码不正确");
        }
        //账号删除/注销
        if (user.getStatue() == 3) {
            throw new LockedAccountException("当前账号已注销");
        }
        //账号锁定
        if (user.getStatue() == 0) {
            throw new LockedAccountException("账号已被锁定,请联系管理员");
        }
        if(true){
            return  new SimpleAuthenticationInfo(user, user.getUsername(), getName());
        }else{
            throw new UnknownAccountException("验证码错误,请核对后重新输入");
        }
    }

    /**
     * 用来判断是否使用当前的 realm
     * @param var1 传入的token
     * @return true就使用，false就不使用
     */
    @Override
    public boolean supports(AuthenticationToken var1) {
        return var1 instanceof UsernamePhoneCodeToken;
    }

}
