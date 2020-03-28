package com.example.shiro.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.common.utils.QueryUtil;
import com.example.shiro.common.utils.ShiroUtils;
import com.example.shiro.web.dao.WebUserDao;
import com.example.shiro.web.entity.WebMenuEntity;
import com.example.shiro.web.entity.WebUserEntity;
import com.example.shiro.web.service.WebUserRoleService;
import com.example.shiro.web.service.WebUserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;


@Service("webUserService")
public class WebUserServiceImpl extends ServiceImpl<WebUserDao, WebUserEntity> implements WebUserService {

    @Autowired
    private WebUserRoleService webUserRoleService;

    @Override
    public List<Long> queryAllMenuId(Long userId) {
        return baseMapper.queryAllMenuId(userId);
    }

    @Override
    public List<WebMenuEntity> queryAllPerms1(Long userId) {
        return baseMapper.queryAllPerms1(userId);
    }

    @Override
//    @DataFilter(subDept = true, user = false)
    public PageUtils queryPage(Map<String, Object> params) {
        String username = (String) params.get("username");

        IPage<WebUserEntity> page = this.page(
                new QueryUtil<WebUserEntity>().getPage(params),
                new QueryWrapper<WebUserEntity>()
                        .like(StringUtils.isNotBlank(username), "username", username)
                        .apply(params.get(Constant.SQL_FILTER) != null, (String) params.get(Constant.SQL_FILTER))
        );

        return new PageUtils(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(WebUserEntity user) {
        user.setInserttime(new Date());
//        user.setPasswd(DigestUtils.md5DigestAsHex((user.getPasswd() + user.getPhoneNumber()).getBytes()));
        user.setPassword(ShiroUtils.sha256(user.getPassword(), user.getUsername()));
        this.save(user);

        //保存用户与角色关系
        webUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void update(WebUserEntity user) {
        if (StringUtils.isBlank(user.getPassword())) {
            user.setPassword(null);
        } else {
            WebUserEntity userEntity = this.getById(user.getUserId());
            user.setPassword(ShiroUtils.sha256(user.getPassword(), userEntity.getUsername()));
        }
        this.updateById(user);

        //保存用户与角色关系
        webUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }


    @Override
    public boolean updatePassword(Long userId, String password, String newPassword) {
        WebUserEntity userEntity = new WebUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,
                new QueryWrapper<WebUserEntity>().eq("user_id", userId).eq("password", password));
    }

    @Override
    public void updateUserRole(WebUserEntity user) {
        //保存用户与角色关系
        webUserRoleService.saveOrUpdate(user.getUserId(), user.getRoleIdList());
    }

    @Override
    public List<WebMenuEntity> queryUrlList(Long userId) {
        return baseMapper.queryUrlList(userId);
    }

    @Override
    public WebUserEntity selectUserById(Long userId) {
        return baseMapper.selectUserById(userId);
    }
}
