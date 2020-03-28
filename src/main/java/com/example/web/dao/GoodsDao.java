package com.example.web.dao;

import com.example.web.entity.GoodsEntity;
import org.apache.ibatis.annotations.Select;
import org.springframework.web.bind.annotation.RequestParam;

public interface GoodsDao {
    @Select("select id,name,num,version,create_time createTime from goods where id = #{id}")
    GoodsEntity selectById(@RequestParam("id")Integer id);
}
