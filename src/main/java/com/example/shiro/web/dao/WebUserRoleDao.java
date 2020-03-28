package com.example.shiro.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro.web.entity.WebUserRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 用户与角色对应关系
 * 
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@Mapper
public interface WebUserRoleDao extends BaseMapper<WebUserRoleEntity> {
    /**
     * 根据用户ID，获取角色ID列表
     */
    List<Long> queryRoleIdList(Long userId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);
}
