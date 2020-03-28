package com.example.shiro.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro.web.entity.WebMenuEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * 菜单管理
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@Mapper
public interface WebMenuDao extends BaseMapper<WebMenuEntity> {
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
}
