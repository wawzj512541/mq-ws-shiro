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
import com.example.shiro.sys.entity.SysRoleEntity;
import com.example.shiro.sys.service.SysRoleDeptService;
import com.example.shiro.sys.service.SysRoleMenuService;
import com.example.shiro.sys.service.SysRoleService;
import com.example.shiro.common.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 角色管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {
    @Autowired
    private SysRoleService sysRoleService;
    @Autowired
    private SysRoleMenuService sysRoleMenuService;
    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:role:list")
    public APIResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = sysRoleService.queryPage(params);

        return APIResponse.returnSuccess(page);
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:role:select")
    public APIResponse select() {
        List<SysRoleEntity> list = sysRoleService.list();

        return APIResponse.returnSuccess(list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:role:info")
    public APIResponse info(Long roleId) {
        SysRoleEntity role = sysRoleService.getById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = sysRoleMenuService.queryMenuIdList(roleId);
//		List<Long> menuIdList = sysRoleMenuService.querySelectMenuIdList(roleId);
        role.setMenuIdList(menuIdList);

        //查询角色对应的部门
        List<Long> deptIdList = sysRoleDeptService.queryDeptIdList(new Long[]{roleId});
//		List<Long> deptIdList = sysRoleDeptService.querySelectDeptIdList(new Long[]{roleId});
        role.setDeptIdList(deptIdList);

        return APIResponse.returnSuccess(role);
    }

    /**
     * 保存角色
     */
    @SysLog(value = "保存角色", type = "sys角色")
    @RequestMapping("/save")
    @RequiresPermissions("sys:role:save")
    public APIResponse save(@Validated SysRoleEntity role) {

        sysRoleService.saveRole(role);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改角色
     */
    @SysLog(value = "修改角色", type = "sys角色")
    @RequestMapping("/update")
    @RequiresPermissions("sys:role:update")
    public APIResponse update(@Validated SysRoleEntity role) {

        sysRoleService.update(role);

        return APIResponse.returnSuccess();
    }

    /**
     * 删除角色
     */
    @SysLog(value = "删除角色", type = "sys角色")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:role:delete")
    public APIResponse delete(Long[] roleIds) {
        sysRoleService.deleteBatch(roleIds);

        return APIResponse.returnSuccess();
    }
}
