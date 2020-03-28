package com.example.shiro.web.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shiro.web.entity.WebRoleMenuEntity;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
public interface WebRoleMenuService extends IService<WebRoleMenuEntity> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

}

