package com.example.shiro.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.web.entity.WebRoleEntity;

import java.util.Map;

/**
 * 角色
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
public interface WebRoleService extends IService<WebRoleEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveRole(WebRoleEntity role);

    void update(WebRoleEntity role);

    void deleteBatch(Long[] roleIds);

    void updateStatus(WebRoleEntity role);
}

