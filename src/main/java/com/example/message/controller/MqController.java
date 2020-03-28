package com.example.message.controller;

import com.example.config.RabbitConfig;
import com.example.web.entity.GoodsEntity;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MqController {

    @Autowired
    private AmqpTemplate amqpTemplate;

    @GetMapping("/send")
    public void send(){
        //往队列里发送消息
//        amqpTemplate.convertAndSend(RabbitConfig.SPRING_BOOT_QUEUE,"hello sprintboot rabbitmq");

        GoodsEntity entity = new GoodsEntity();
        entity.setId(1);
        entity.setName("狗粮");
        entity.setNum(100);
        //往交换机发送消息
        amqpTemplate.convertAndSend(RabbitConfig.SPRING_BOOT_QUEUE,entity);
        amqpTemplate.convertAndSend(RabbitConfig.SPRING_BOOT_EXCHANGE,RabbitConfig.SPRING_BOOT_BIND_KEY,entity);
    }
}
