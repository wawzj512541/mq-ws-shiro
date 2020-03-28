/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package com.example.shiro.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.common.annotation.DataFilter;
import com.example.shiro.sys.dao.SysDeptDao;
import com.example.shiro.sys.entity.SysDeptEntity;
import com.example.shiro.sys.entity.SysRoleDeptEntity;
import com.example.shiro.sys.service.SysDeptService;
import com.example.shiro.sys.service.SysRoleDeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service("sysDeptService")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao, SysDeptEntity> implements SysDeptService {
    @Autowired
    SysRoleDeptService sysRoleDeptService;

    @Override
    @DataFilter(subDept = true, user = false, tableAlias = "t1")
    public List<SysDeptEntity> queryList(Map<String, Object> params) {
        return baseMapper.queryList(params);
    }

    @Override
    public List<Long> queryDetpIdList(Long parentId) {
        return baseMapper.queryDetpIdList(parentId);
    }

    @Override
    public List<Long> getSubDeptIdList(Long deptId) {
        //部门及子部门ID列表
        List<Long> deptIdList = new ArrayList<>();

        //获取子部门ID
        List<Long> subIdList = queryDetpIdList(deptId);
        getDeptTreeList(subIdList, deptIdList);

        return deptIdList;
    }

    @Override
    public void delete(long deptId) {
        //删除菜单
        this.removeById(deptId);
        //删除部门与角色关联
        sysRoleDeptService.remove(new QueryWrapper<SysRoleDeptEntity>().eq("dept_id", deptId));
        sysRoleDeptService.remove(new QueryWrapper<SysRoleDeptEntity>().eq("select_dept_id", deptId));
    }

    /**
     * 递归
     */
    private void getDeptTreeList(List<Long> subIdList, List<Long> deptIdList) {
        for (Long deptId : subIdList) {
            List<Long> list = queryDetpIdList(deptId);
            if (list.size() > 0) {
                getDeptTreeList(list, deptIdList);
            }

            deptIdList.add(deptId);
        }
    }
}
