package com.example.shiro.web.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.web.dao.WebRoleMenuDao;
import com.example.shiro.web.entity.WebRoleMenuEntity;
import com.example.shiro.web.service.WebRoleMenuService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service("webRoleMenuService")
public class WebRoleMenuServiceImpl extends ServiceImpl<WebRoleMenuDao, WebRoleMenuEntity> implements WebRoleMenuService {


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
        //先删除角色与菜单关系
        deleteBatch(new Long[]{roleId});

        if (menuIdList.size() == 0) {
            return;
        }

        //保存角色与菜单关系
        for (Long menuId : menuIdList) {
            WebRoleMenuEntity webRoleMenuEntity = new WebRoleMenuEntity();
            webRoleMenuEntity.setMenuId(menuId);
            webRoleMenuEntity.setRoleId(roleId);

            this.save(webRoleMenuEntity);
        }
    }

    @Override
    public List<Long> queryMenuIdList(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }

}
