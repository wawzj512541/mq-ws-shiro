package com.example.shiro.web.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.example.shiro.web.entity.WebMenuEntity;

import java.util.List;

/**
 * 菜单管理
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
public interface WebMenuService extends IService<WebMenuEntity> {


    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId   父菜单ID
     * @param menuIdList 用户菜单ID
     */
    List<WebMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList);

    /**
     * 根据父菜单，查询子菜单
     *
     * @param parentId 父菜单ID
     */
    List<WebMenuEntity> queryListParentId(Long parentId);

    /**
     * 获取不包含按钮的菜单列表
     */
    List<WebMenuEntity> queryNotButtonList();

    /**
     * 获取用户菜单列表
     */
    List<WebMenuEntity> getUserMenuList(Long userId);

    /**
     * 删除
     */
    void delete(Long menuId);

    List<WebMenuEntity> selectAll();
    List<WebMenuEntity> queryAllPerms1(Long userId);

    List<WebMenuEntity> queryUrlList(Long userId);
}

