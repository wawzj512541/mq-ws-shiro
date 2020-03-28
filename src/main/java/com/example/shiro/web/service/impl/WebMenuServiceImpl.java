package com.example.shiro.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.web.dao.WebMenuDao;
import com.example.shiro.web.entity.WebMenuEntity;
import com.example.shiro.web.entity.WebRoleMenuEntity;
import com.example.shiro.web.service.WebMenuService;
import com.example.shiro.web.service.WebRoleMenuService;
import com.example.shiro.web.service.WebUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service("webMenuService")
public class WebMenuServiceImpl extends ServiceImpl<WebMenuDao, WebMenuEntity> implements WebMenuService {

    @Autowired
    private WebUserService webUserService;
    @Autowired
    private WebRoleMenuService webRoleMenuService;

    @Override
    public List<WebMenuEntity> queryListParentId(Long parentId, List<Long> menuIdList) {
        List<WebMenuEntity> menuList = queryListParentId(parentId);
        if (menuIdList == null) {
            return menuList;
        }

        List<WebMenuEntity> userMenuList = new ArrayList<>();
        for (WebMenuEntity menu : menuList) {
            if (menuIdList.contains(menu.getMenuId())) {
                userMenuList.add(menu);
            }
        }
        return userMenuList;
    }

    @Override
    public List<WebMenuEntity> queryListParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }


    @Override
    public List<WebMenuEntity> queryNotButtonList() {
        return baseMapper.queryNotButtonList();
    }

    @Override
    public List<WebMenuEntity> getUserMenuList(Long userId) {
        //系统管理员，拥有最高权限
        if (userId == Constant.SUPER_ADMIN) {
            return getAllMenuList(null);
        }

        //用户菜单列表
        List<Long> menuIdList = webUserService.queryAllMenuId(userId);
        return getAllMenuList(menuIdList);
    }


    @Override
    public void delete(Long menuId) {
        //删除菜单
        this.removeById(menuId);
        //删除菜单与角色关联
        webRoleMenuService.remove(new QueryWrapper<WebRoleMenuEntity>().eq("menu_id", menuId));
    }

    @Override
    public List<WebMenuEntity> selectAll() {
        return getAllMenuList(null);
    }

    /**
     * 获取所有菜单列表
     */
    private List<WebMenuEntity> getAllMenuList(List<Long> menuIdList) {
        //查询根菜单列表
        List<WebMenuEntity> menuList = queryListParentId(0L, menuIdList);
        //递归获取子菜单
        getMenuTreeList(menuList, menuIdList);

        return menuList;
    }

    /**
     * 递归
     */
    private List<WebMenuEntity> getMenuTreeList(List<WebMenuEntity> menuList, List<Long> menuIdList) {
        List<WebMenuEntity> subMenuList = new ArrayList<WebMenuEntity>();

        for (WebMenuEntity entity : menuList) {
            //目录
            if (entity.getType() == Constant.MenuType.CATALOG.getValue()) {
                entity.setList(getMenuTreeList(queryListParentId(entity.getMenuId(), menuIdList), menuIdList));
            }
            subMenuList.add(entity);
        }

        return subMenuList;
    }

    @Override
    public List<WebMenuEntity> queryAllPerms1(Long userId) {
        return webUserService.queryAllPerms1(userId);
    }

    @Override
    public List<WebMenuEntity> queryUrlList(Long userId){
        return webUserService.queryUrlList(userId);
    }
}
