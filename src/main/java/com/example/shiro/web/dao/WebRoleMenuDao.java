package com.example.shiro.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro.web.entity.WebRoleMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 角色与菜单对应关系
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@Mapper
public interface WebRoleMenuDao extends BaseMapper<WebRoleMenuEntity> {
    /**
     * 根据角色ID，获取菜单ID列表
     */
    List<Long> queryMenuIdList(Long roleId);

    /**
     * 根据角色ID数组，批量删除
     */
    int deleteBatch(Long[] roleIds);

    @Select("select select_menu_id from web_role_menu where role_id = #{roleId} and select_menu_id >=0")
    List<Long> querySelectMenuIdList(Long roleId);
}
