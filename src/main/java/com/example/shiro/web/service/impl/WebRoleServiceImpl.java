package com.example.shiro.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.common.utils.QueryUtil;
import com.example.shiro.web.dao.WebRoleDao;
import com.example.shiro.web.entity.WebRoleEntity;
import com.example.shiro.web.service.WebRoleMenuService;
import com.example.shiro.web.service.WebRoleService;
import com.example.shiro.web.service.WebUserRoleService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;


@Service("webRoleService")
public class WebRoleServiceImpl extends ServiceImpl<WebRoleDao, WebRoleEntity> implements WebRoleService {

    @Autowired
    private WebRoleMenuService webRoleMenuService;
    @Autowired
    private WebUserRoleService webUserRoleService;

    @Override
//    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String roleName = (String) params.get("roleName");

        IPage<WebRoleEntity> page = this.page(
                new QueryUtil<WebRoleEntity>().getPage(params),
                new QueryWrapper<WebRoleEntity>()
                        .like(StringUtils.isNotBlank(roleName), "role_name", roleName)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );


        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(WebRoleEntity role) {
        role.setCreateTime(new Date());
        role.setCreateTime(new Date());
        this.save(role);

        //保存角色与菜单关系
        webRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WebRoleEntity role) {
        role.setCreateTime(new Date());
        this.updateById(role);

        //更新角色与菜单关系
        webRoleMenuService.saveOrUpdate(role.getRoleId(), role.getMenuIdList());

    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleIds) {
        //删除角色
        this.removeByIds(Arrays.asList(roleIds));

        //删除角色与菜单关联
        webRoleMenuService.deleteBatch(roleIds);

        //删除角色与用户关联
        webUserRoleService.deleteBatch(roleIds);
    }

    @Override
    public void updateStatus(WebRoleEntity role) {
        baseMapper.updateStatus(role);
    }


}
