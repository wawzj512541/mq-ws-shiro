/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package com.example.shiro.sys.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro.sys.entity.SysUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 系统用户
 *
 * @author Mark sunlightcs@gmail.com
 */
@Mapper
public interface SysUserDao extends BaseMapper<SysUserEntity> {
	
	/**
	 * 查询用户的所有权限
	 * @param userId  用户ID
	 */
	@Select("select m.perms from sys_user_role ur  LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
			"LEFT JOIN sys_menu m on rm.menu_id = m.menu_id " +
			"where ur.user_id = #{userId}")
	List<String> queryAllPerms(Long userId);
	
	/**
	 * 查询用户的所有菜单ID
	 */
	@Select("select distinct rm.menu_id from sys_user_role ur " +
			"LEFT JOIN sys_role_menu rm on ur.role_id = rm.role_id " +
			"where ur.user_id = #{userId} and rm.menu_id >=0")
	List<Long> queryAllMenuId(Long userId);

	@Select("SELECT su.*,GROUP_CONCAT(sr.role_id order by sr.role_id separator '_') roleIds " +
			"FROM `sys_user` su,sys_user_role sr where su.user_id = sr.user_id and su.username=#{username} group by su.user_id")
	SysUserEntity selectUserByUsername(String username);
}
