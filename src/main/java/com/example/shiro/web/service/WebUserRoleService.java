package com.example.shiro.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shiro.web.entity.WebUserRoleEntity;

import java.util.List;

/**
 * 用户与角色对应关系
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
public interface WebUserRoleService extends IService<WebUserRoleEntity> {

    void saveOrUpdate(Long userId, List<Long> roleIdList);

    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

}

