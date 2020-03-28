package com.example.shiro.web.controller;

import com.example.shiro.common.annotation.SysLog;
import com.example.shiro.common.result.APIResponse;
import com.example.shiro.common.shiro.Constant;
import com.example.shiro.common.RRException;
import com.example.shiro.sys.controller.AbstractController;
import com.example.shiro.web.entity.WebMenuEntity;
import com.example.shiro.web.service.WebMenuService;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * 菜单管理
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@RestController
@RequestMapping("web/webmenu")
public class WebMenuController extends AbstractController {
    @Autowired
    private WebMenuService webMenuService;

    /**
     * 导航菜单
     */
    @RequestMapping("/nav")
    public APIResponse nav() {
        List<WebMenuEntity> menuList = webMenuService.getUserMenuList(getId());
//        List<WebMenuEntity> menuList = webMenuService.selectAll();
        return APIResponse.returnSuccess(menuList);
    }


    /**
     * 所有菜单列表
     */
    @RequestMapping("/list")
    @RequiresPermissions("web:menu:list")
    public APIResponse list() {
        List<WebMenuEntity> menuList = webMenuService.list();
        for (WebMenuEntity webMenuEntity : menuList) {
            WebMenuEntity parentMenuEntity = webMenuService.getById(webMenuEntity.getParentId());
            if (parentMenuEntity != null) {
                webMenuEntity.setParentName(parentMenuEntity.getName());
            }
        }

        return APIResponse.returnSuccess(menuList);
    }

    /**
     * 选择菜单(添加、修改菜单)
     */
    @RequestMapping("/select")
    @RequiresPermissions("web:menu:select")
    public APIResponse select() {
        //查询列表数据
        List<WebMenuEntity> menuList = webMenuService.queryNotButtonList();

        //添加顶级菜单
        WebMenuEntity root = new WebMenuEntity();
        root.setMenuId(0L);
        root.setName("一级菜单");
        root.setParentId(-1L);
        root.setOpen(true);
        menuList.add(root);

        return APIResponse.returnSuccess(menuList);
    }

    /**
     * 菜单信息
     */
    @RequestMapping("/info/{menuId}")
    @RequiresPermissions("web:menu:info")
    public APIResponse info(@PathVariable("menuId") Long menuId) {
        WebMenuEntity menu = webMenuService.getById(menuId);
        return APIResponse.returnSuccess(menu);
    }

    /**
     * 保存
     */
    @SysLog(value = "保存菜单", type = "web菜单")
    @RequestMapping("/save")
    @RequiresPermissions("web:menu:save")
    public APIResponse save(WebMenuEntity menu) {
        //数据校验
        verifyForm(menu);

        webMenuService.save(menu);

        return APIResponse.returnSuccess();
    }

    /**
     * 修改
     */
    @SysLog(value = "修改菜单", type = "web菜单")
    @RequestMapping("/update")
    @RequiresPermissions("web:menu:update")
    public APIResponse update(WebMenuEntity menu) {
        //数据校验
        verifyForm(menu);

        webMenuService.updateById(menu);

        return APIResponse.returnSuccess();
    }

    /**
     * 删除
     */
    @SysLog(value = "删除菜单", type = "web菜单")
    @RequestMapping("/delete")
    @RequiresPermissions("web:menu:delete")
    public APIResponse delete(long menuId) {
        if (menuId <= 31) {
            return APIResponse.returnFail("系统菜单，不能删除");
        }

        //判断是否有子菜单或按钮
        List<WebMenuEntity> menuList = webMenuService.queryListParentId(menuId);
        if (menuList.size() > 0) {
            return APIResponse.returnFail("请先删除子菜单或按钮");
        }

        webMenuService.delete(menuId);

        return APIResponse.returnSuccess();
    }

    /**
     * 验证参数是否正确
     */
    private void verifyForm(WebMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            throw new RRException("菜单名称不能为空");
        }

        if (menu.getParentId() == null) {
            throw new RRException("上级菜单不能为空");
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                throw new RRException("菜单URL不能为空");
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();
        if (menu.getParentId() != 0) {
            WebMenuEntity parentMenu = webMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() ||
                menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                throw new RRException("上级菜单只能为目录类型");
            }
            return;
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                throw new RRException("上级菜单只能为菜单类型");
            }
            return;
        }
    }

}
