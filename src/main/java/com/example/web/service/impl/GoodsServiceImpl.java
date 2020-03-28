package com.example.web.service.impl;

import com.example.common.CacheNameSpace;
import com.example.common.annotation.QueryCache;
import com.example.common.annotation.QueryCacheKey;
import com.example.web.dao.GoodsDao;
import com.example.web.entity.GoodsEntity;
import com.example.web.service.GoodsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service("googsService")
public class GoodsServiceImpl implements GoodsService {
    @Resource
    GoodsDao goodsDao;

    @Override
    @QueryCache(nameSpace = CacheNameSpace.SHOP_GOODS,expire = 30)
    public GoodsEntity selectById(@QueryCacheKey Integer id) {
        GoodsEntity goodsEntity = goodsDao.selectById(id);
        return goodsEntity;
    }
}
