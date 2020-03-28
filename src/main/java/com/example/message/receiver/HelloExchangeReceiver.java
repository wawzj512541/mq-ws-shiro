package com.example.message.receiver;

import com.example.config.RabbitConfig;
import com.example.web.entity.GoodsEntity;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(bindings = @QueueBinding(
        value = @Queue(name = RabbitConfig.SPRING_BOOT_EXCHANGE_queue, durable = "true"),
        exchange = @Exchange(name = RabbitConfig.SPRING_BOOT_EXCHANGE,
                type = ExchangeTypes.TOPIC,
                ignoreDeclarationExceptions = "true"),
        key = {RabbitConfig.SPRING_BOOT_EXCHANGE_queue}
))
public class HelloExchangeReceiver {
    @RabbitHandler
    public void process(GoodsEntity entity) {
        System.out.println("You receiver a message from exchangeQueue: --> " + entity);
    }
}
