package com.example.message.receiver;

import com.example.config.RabbitConfig;
import com.example.web.entity.GoodsEntity;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.SPRING_BOOT_QUEUE)
public class HelloReceiver {
    @RabbitHandler
    public void process(GoodsEntity entity) {
        System.out.println("You receiver a message from exchangeQueue: --> " + entity);
    }
}
