package com.example.message.receiver;

import com.example.config.RabbitConfig;
import com.example.shiro.sys.entity.SysUserEntity;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = RabbitConfig.SYS_LOGIN_QUEUE)
public class SysLoginReceiver {


    @RabbitHandler
    public void process(SysUserEntity entity) {
        System.out.println("web用户登录 --> " + entity);
        System.out.println("准备获取历史通知 -->");

    }
}
