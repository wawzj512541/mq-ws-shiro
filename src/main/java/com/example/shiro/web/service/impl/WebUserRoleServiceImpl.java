package com.example.shiro.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.web.dao.WebUserDao;
import com.example.shiro.web.dao.WebUserRoleDao;
import com.example.shiro.web.entity.WebUserRoleEntity;
import com.example.shiro.web.service.WebUserRoleService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;


@Service("webUserRoleService")
public class WebUserRoleServiceImpl extends ServiceImpl<WebUserRoleDao, WebUserRoleEntity> implements WebUserRoleService {

    @Override
    public void saveOrUpdate(Long userId, List<Long> roleIdList) {
        //先删除用户与角色关系
        this.remove(new QueryWrapper<WebUserRoleEntity>().eq("user_id", userId));

        if (roleIdList == null || roleIdList.size() == 0) {
            return;
        }

        //保存用户与角色关系
        for (Long roleId : roleIdList) {
            WebUserRoleEntity webUserRoleEntity = new WebUserRoleEntity();
            webUserRoleEntity.setUserId(userId);
            webUserRoleEntity.setRoleId(roleId);

            this.save(webUserRoleEntity);
        }

    }

    @Override
    public List<Long> queryRoleIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }

    @Override
    public int deleteBatch(Long[] roleIds) {
        return baseMapper.deleteBatch(roleIds);
    }


}
