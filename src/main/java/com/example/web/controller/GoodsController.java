package com.example.web.controller;

import com.example.config.RabbitConfig;
import com.example.web.entity.GoodsEntity;
import com.example.web.service.GoodsService;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/goods")
public class GoodsController {
    @Autowired
    GoodsService goodsService;

    @Autowired
    AmqpTemplate amqpTemplate;

    @GetMapping("/selectById")
    public GoodsEntity selectById(Integer id){
        GoodsEntity entity = goodsService.selectById(id);
        amqpTemplate.convertAndSend(RabbitConfig.SPRING_BOOT_QUEUE,entity);
        amqpTemplate.convertAndSend(RabbitConfig.SPRING_BOOT_EXCHANGE,RabbitConfig.SPRING_BOOT_BIND_KEY,entity);
        return entity;
    }
}
