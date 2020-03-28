package com.example.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    // 队列名称
    public final static String SPRING_BOOT_QUEUE = "spring-boot-queue-1";
    //sys用户登录队列
    public final static String SYS_LOGIN_QUEUE = "mqDemo-sys-login";
    //web用户登录队列
    public final static String WEB_LOGIN_QUEUE = "mqDemo-web-login";
    // 交换机队列名称
    public final static String SPRING_BOOT_EXCHANGE_queue = "spring-boot-topic-exchange-queue-1";
    // 交换机名称
    public final static String SPRING_BOOT_EXCHANGE = "spring-boot-topic-exchange-1";
    // 绑定的值
    public static final String SPRING_BOOT_BIND_KEY = "topic.message";


    @Bean
    public Queue helloQueue() {
        return new Queue(SPRING_BOOT_QUEUE);
    }

    @Bean
    public Queue sysLoginQueue() {
        return new Queue(SYS_LOGIN_QUEUE);
    }

    @Bean
    public Queue webLoginQueue() {
        return new Queue(WEB_LOGIN_QUEUE);
    }

    @Bean
    public Queue exQueue() {
        return new Queue(SPRING_BOOT_EXCHANGE_queue);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(SPRING_BOOT_EXCHANGE);
    }

    @Bean
    Binding bindingExchangeMessage(Queue exQueue, TopicExchange exchange) {
        return BindingBuilder.bind(exQueue).to(exchange).with(SPRING_BOOT_BIND_KEY);
    }


}
