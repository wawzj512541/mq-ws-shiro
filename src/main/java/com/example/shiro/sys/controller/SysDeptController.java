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
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.sys.entity.SysDeptEntity;
import com.example.shiro.sys.service.SysDeptService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;


/**
 * 部门管理
 *
 * @author Mark sunlightcs@gmail.com
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController {
    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("sys:dept:list")
    public APIResponse list() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());

        return APIResponse.returnSuccess(deptList);
    }

    /**
     * 选择部门(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("sys:dept:select")
    public APIResponse select() {
        List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<>());

        //添加一级部门
        if (getUserId() == Constant.SUPER_ADMIN) {
            SysDeptEntity root = new SysDeptEntity();
            root.setDeptId(0L);
            root.setName("一级部门");
            root.setParentId(-1L);
            root.setOpen(true);
            deptList.add(root);
        }

        return APIResponse.returnSuccess(deptList);
    }

    /**
     * 上级部门Id(管理员则为0)
     */
    @RequestMapping("/info")
    @RequiresPermissions("sys:dept:list")
    public APIResponse info() {
        long deptId = 0;
        if (getUserId() != Constant.SUPER_ADMIN) {
            List<SysDeptEntity> deptList = sysDeptService.queryList(new HashMap<String, Object>());
            Long parentId = null;
            for (SysDeptEntity sysDeptEntity : deptList) {
                if (parentId == null) {
                    parentId = sysDeptEntity.getParentId();
                    continue;
                }

                if (parentId > sysDeptEntity.getParentId()) {
                    parentId = sysDeptEntity.getParentId();
                }
            }
            deptId = parentId;
        }

        return APIResponse.returnSuccess(deptId);
    }

    /**
     * 信息
     */
    @RequestMapping("/info/{deptId}")
    @RequiresPermissions("sys:dept:info")
    public APIResponse info(@PathVariable("deptId") Long deptId) {
        SysDeptEntity dept = sysDeptService.getById(deptId);

        return APIResponse.returnSuccess(dept);
    }

    /**
     * 保存
     */
    @SysLog(value = "保存部门", type = "sys部门")
    @RequestMapping("/save")
    @RequiresPermissions("sys:dept:save")
    public APIResponse save(SysDeptEntity dept) {
        sysDeptService.save(dept);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改
     */
    @SysLog(value = "修改部门", type = "sys部门")
    @RequestMapping("/update")
    @RequiresPermissions("sys:dept:update")
    public APIResponse update(SysDeptEntity dept) {
        sysDeptService.updateById(dept);

        return APIResponse.returnSuccess();
    }

    /**
     * 删除
     */
    @SysLog(value = "删除部门", type = "sys部门")
    @RequestMapping("/delete")
    @RequiresPermissions("sys:dept:delete")
    public APIResponse delete(long deptId) {
        //判断是否有子部门
        List<Long> deptList = sysDeptService.queryDetpIdList(deptId);
        if (deptList.size() > 0) {
            return APIResponse.returnFail("请先删除子部门");
        }

//		sysDeptService.removeById(deptId);
        sysDeptService.delete(deptId);
        return APIResponse.returnSuccess();
    }

}
