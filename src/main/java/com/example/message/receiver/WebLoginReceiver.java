package com.example.message.receiver;

import com.example.config.RabbitConfig;
import com.example.shiro.web.entity.WebUserEntity;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class WebLoginReceiver {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @RabbitHandler
    @RabbitListener(queues = RabbitConfig.WEB_LOGIN_QUEUE)
    public void process(WebUserEntity entity) {
        System.out.println("web用户登录 --> " + entity.getUsername());
        System.out.println("准备获取历史通知 -->");
        messagingTemplate.convertAndSend("/topic", entity.toString());
    }
}
