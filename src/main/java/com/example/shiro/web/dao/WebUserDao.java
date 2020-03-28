package com.example.shiro.web.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.shiro.web.entity.WebMenuEntity;
import com.example.shiro.web.entity.WebUserEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * 用户信息表
 *
 * @author lee
 * @email wawzj512541@gmail.com
 * @date 2020-01-09 15:14:06
 */
@Mapper
public interface WebUserDao extends BaseMapper<WebUserEntity> {

    List<String> queryAllPerms(Long userId);

    /**
     * 查询用户的所有菜单ID
     */
    List<Long> queryAllMenuId(Long userId);

    List<WebMenuEntity> queryAllPerms1(Long userId);

    List<WebMenuEntity> queryUrlList(Long userId);

    @Select("select ui.user_id,ci.company_id,ui.state,ui.type,ui.real_name,ui.sex,ui.email,ui.birthdate,ui.work_unit,ui.account_head,ui.identity,ci.company_name,case ui.type when 2 then ci.province else ui.province end province,case ui.type when 2 then ci.city else ui.city end city,ui.phone_number,ui.passwd,ci.company_providers,ci.company_email " +
            "from user_info ui left JOIN company_info ci on ui.user_id = ci.link_user_id where phone_number=#{phoneNumber}")
    WebUserEntity selectUserByPhone(String phoneNumber);

    @Select("select ui.user_id,ci.company_id,ui.type,ui.real_name,ui.sex,ui.email,ui.birthdate,ui.work_unit,ui.account_head,ui.identity,ci.company_name,case ui.type when 2 then ci.province else ui.province end province,case ui.type when 2 then ci.city else ui.city end city,ui.phone_number,ui.passwd,ci.company_providers,ci.company_email " +
            "from user_info ui left JOIN company_info ci on ui.user_id = ci.link_user_id where ui.user_id=#{id}")
    WebUserEntity selectUserById(Long id);
}
