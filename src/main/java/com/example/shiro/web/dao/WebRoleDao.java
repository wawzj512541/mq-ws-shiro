package com.example.shiro.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro.web.entity.WebRoleEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Update;

/**
 * 角色
 * 
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@Mapper
public interface WebRoleDao extends BaseMapper<WebRoleEntity> {
    @Update("update web_role set status = #{status} where role_id = #{roleId}")
	void updateStatus(WebRoleEntity role);
}
