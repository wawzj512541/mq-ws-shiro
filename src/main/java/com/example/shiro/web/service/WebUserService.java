package com.example.shiro.web.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.web.entity.WebMenuEntity;
import com.example.shiro.web.entity.WebUserEntity;

import java.util.List;
import java.util.Map;

/**
 * 用户信息表
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
public interface WebUserService extends IService<WebUserEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    List<WebMenuEntity> queryAllPerms1(Long userId);

    /**
     * 保存用户
     */
    void saveUser(WebUserEntity user);

    /**
     * 修改用户
     */
    void update(WebUserEntity user);

    /**
     * 修改密码
     *
     * @param userId      用户ID
     * @param password    原密码
     * @param newPassword 新密码
     */
    boolean updatePassword(Long userId, String password, String newPassword);

    void updateUserRole(WebUserEntity user);

    List<WebMenuEntity> queryUrlList(Long userId);

    WebUserEntity selectUserById(Long userId);
}

