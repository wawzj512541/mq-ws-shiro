package com.example.shiro.web.controller;

import com.example.common.annotation.SysLog;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.common.utils.Assert;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.sys.controller.AbstractController;
import com.example.shiro.web.entity.WebUserEntity;
import com.example.shiro.web.service.WebUserRoleService;
import com.example.shiro.web.service.WebUserService;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * 用户信息表
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@RestController
@RequestMapping("web/webuser")
public class WebUserController extends AbstractController {
    @Autowired
    private WebUserService webUserService;
    @Autowired
    private WebUserRoleService webUserRoleService;

    /**
     * 所有用户列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("web:user:list")
    public APIResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = webUserService.queryPage(params);

        return APIResponse.returnSuccess(page);
    }

    /**
     * 获取登录的用户信息
     */
    @RequestMapping("/info")
    public APIResponse info() {
        return APIResponse.returnSuccess(getWebUser());
    }

    /**
     * 修改登录用户密码
     */
    @SysLog(value = "修改登录用户密码", type = "web用户")
    @RequestMapping("/password")
    public APIResponse password(String password, String newPassword) {
        Assert.isBlank(newPassword, "新密码不为能空");

        //原密码
//        password = ShiroUtils.sha256(password, getWebUser().getPhoneNumber());
        password = DigestUtils.md5DigestAsHex((password + getWebUser().getUsername()).getBytes());
        //新密码
//        newPassword = ShiroUtils.sha256(newPassword, getWebUser().getPhoneNumber());
        newPassword = DigestUtils.md5DigestAsHex((newPassword + getWebUser().getUsername()).getBytes());
        //更新密码
        boolean flag = webUserService.updatePassword(getId(), password, newPassword);
        if (!flag) {
            return APIResponse.returnFail("原密码不正确");
        }

        return APIResponse.returnSuccess();
    }

    /**
     * 用户信息
     */
    @RequestMapping("/userInfo")
    @RequiresPermissions("web:user:info")
    public APIResponse info(Long userId) {
        WebUserEntity user = webUserService.selectUserById(userId);

        //获取用户所属的角色列表
        List<Long> roleIdList = webUserRoleService.queryRoleIdList(userId);
        user.setRoleIdList(roleIdList);

        return APIResponse.returnSuccess(user);
    }

    /**
     * 保存用户
     */
    @SysLog(value = "保存用户", type = "web用户")
    @RequestMapping("/save")
    @RequiresPermissions("web:user:save")
    public APIResponse save(@Validated WebUserEntity user) {

        webUserService.saveUser(user);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改用户
     */
    @SysLog(value = "修改用户", type = "web用户")
    @RequestMapping("/update")
    @RequiresPermissions("web:user:update")
    public APIResponse update(@Validated WebUserEntity user) {

        webUserService.update(user);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改用户角色
     */
    @SysLog(value = "修改用户角色", type = "web用户")
    @PostMapping("/updateUserRole")
    @RequiresPermissions("web:user:update")
    public APIResponse updateUserRole(WebUserEntity user) {
        Assert.isNull(user.getUserId(), "用户id不能为空");
        Assert.isNull(user.getRoleIdList(), "角色列表不能为空");
        webUserService.updateUserRole(user);
        return APIResponse.returnSuccess();
    }

    /**
     * 删除用户
     */
    @SysLog(value = "删除用户", type = "web用户")
    @RequestMapping("/delete")
    @RequiresPermissions("web:user:delete")
    public APIResponse delete(Long[] userIds) {
        if (ArrayUtils.contains(userIds, 1L)) {
            return APIResponse.returnFail("系统管理员不能删除");
        }

        if (ArrayUtils.contains(userIds, getId())) {
            return APIResponse.returnFail("当前用户不能删除");
        }

        webUserService.removeByIds(Arrays.asList(userIds));

        return APIResponse.returnSuccess();
    }

}
