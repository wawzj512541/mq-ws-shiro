package com.example.shiro.web.controller;

import com.example.common.annotation.SysLog;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.common.utils.Assert;
import com.example.shiro.common.utils.PageUtils;
import com.example.shiro.common.utils.UserValidator;
import com.example.shiro.sys.controller.AbstractController;
import com.example.shiro.web.entity.WebRoleEntity;
import com.example.shiro.web.service.WebRoleMenuService;
import com.example.shiro.web.service.WebRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;


/**
 * 角色
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@RestController
@RequestMapping("web/webrole")
public class WebRoleController extends AbstractController {
    @Autowired
    private WebRoleService webRoleService;
    @Autowired
    private WebRoleMenuService webRoleMenuService;

    /**
     * 角色列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("web:role:list")
    public APIResponse list(@RequestParam Map<String, Object> params) {
        PageUtils page = webRoleService.queryPage(params);

        return APIResponse.returnSuccess(page);
    }

    /**
     * 角色列表
     */
    @RequestMapping("/select")
    @RequiresPermissions("web:role:select")
    public APIResponse select() {
        List<WebRoleEntity> list = webRoleService.list();

        return APIResponse.returnSuccess(list);
    }

    /**
     * 角色信息
     */
    @RequestMapping("/info")
    @RequiresPermissions("web:role:info")
    public APIResponse info(Long roleId) {
        WebRoleEntity role = webRoleService.getById(roleId);

        //查询角色对应的菜单
        List<Long> menuIdList = webRoleMenuService.queryMenuIdList(roleId);
//        List<Long> menuIdList = webRoleMenuService.querySelectMenuIdList(roleId);
        role.setMenuIdList(menuIdList);


        return APIResponse.returnSuccess(role);
    }

    /**
     * 保存角色
     */
    @SysLog(value = "保存角色", type = "web角色")
    @RequestMapping("/save")
    @RequiresPermissions("web:role:save")
    public APIResponse save(WebRoleEntity role) {
        UserValidator.validateEntity(role);

        role.setModifyBy(getUser().getUsername());
        webRoleService.saveRole(role);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改角色
     */
    @SysLog(value = "修改角色", type = "web角色")
    @RequestMapping("/update")
    @RequiresPermissions("web:role:update")
    public APIResponse update(WebRoleEntity role) {
        UserValidator.validateEntity(role);
        role.setModifyBy(getUser().getUsername());
        webRoleService.update(role);

        return APIResponse.returnSuccess();
    }

    /**
     * 删除角色
     */
    @SysLog(value = "删除角色", type = "web角色")
    @RequestMapping("/delete")
    @RequiresPermissions("web:role:delete")
    public APIResponse delete(Long[] roleIds) {
        webRoleService.deleteBatch(roleIds);

        return APIResponse.returnSuccess();
    }

    @SysLog(value = "修改角色状态", type = "web角色")
    @PostMapping("/updateStatus")
    @RequiresPermissions("web:role:update")
    public APIResponse updateStatus(WebRoleEntity role) {
        Assert.isNull(role.getRoleId(), "id不能为空！");
        Assert.isNull(role.getStatus(), "状态不能为空！");
        webRoleService.updateStatus(role);
        return APIResponse.returnSuccess();
    }
}
