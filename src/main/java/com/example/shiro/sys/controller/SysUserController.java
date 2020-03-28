/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.shiro.sys.controller;


import com.example.shiro.common.annotation.SysLog;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.sys.entity.SysUserEntity;
import com.example.shiro.sys.service.SysUserRoleService;
import com.example.shiro.sys.service.SysUserService;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.common.utils.ShiroUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/user")
public class SysUserController extends AbstractController {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysUserRoleService sysUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:user:list")
    public APIResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysUserService.queryPage(params);

        return APIResponse.returnSuccess(page);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public APIResponse info() {
        return APIResponse.returnSuccess(getUser());
    }

    /**
     * 修改登录用户密码
     */
    @RequestMapping("/password")
    public APIResponse password(String password, String newPassword) {
        if (StringUtils.isBlank(newPassword)) {
            return APIResponse.returnFail("新密码不为能空");
        }
        //原密码
        password = ShiroUtils.sha256(password, getUser().getSalt());
        //新密码
        newPassword = ShiroUtils.sha256(newPassword, getUser().getSalt());

        //更新密码
        boolean flag = sysUserService.updatePassword(getUserId(), password, newPassword);
        if (!flag) {
            return APIResponse.returnFail("原密码不正确");
        }

        return APIResponse.returnSuccess();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/userInfo")
    @RequiresPermissions("sys:user:info")
    public APIResponse info(Long userId) {
        SysUserEntity user = sysUserService.getById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = sysUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return APIResponse.returnSuccess(user);
    }

    /**
     * 保存用户
     */
    @SysLog(value = "保存用户", type = "sys用户")
    @RequestMapping("/save")
    @RequiresPermissions("sys:user:save")
    public APIResponse save(@Validated SysUserEntity user) {

        sysUserService.saveUser(user);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改用户
     */
    @SysLog(value = "修改用户", type = "sys用户")
    @RequestMapping("/update")
    @RequiresPermissions("sys:user:update")
    public APIResponse update(@Validated SysUserEntity user) {
        sysUserService.update(user);
        return APIResponse.returnSuccess();
    }

    /**
     * 删除用户
     */
    @SysLog(value = "删除用户", type = "sys用户")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:user:delete")
    public APIResponse delete(Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return APIResponse.returnFail("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getUserId())) {
            return APIResponse.returnFail("当前用户不能删除");
        }

        sysUserService.removeByIds(Arrays.asList(userIds));

        return APIResponse.returnSuccess();
    }
}
