package com.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * @Description:
 * @Author: vesus
 * @CreateDate: 2018/5/29 上午10:41
 * @Version: 1.0
 */
@Configuration
//注解表示开启使用STOMP协议来传输基于代理的消息，Broker就是代理的意思。
//控制器支持使用@MessageMapping,就像使用@RequestMapping一样
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    /***
     * 注册 Stomp的端点
     * addEndpoint：添加STOMP协议的访问端点。提供WebSocket或SockJS客户端访问的地址，客户端打开双通道时需要的url
     * setAllowedOrigins：允许所有的域名跨域访问
     * withSockJS：使用SockJS协议
     * @param registry
     */
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/websocket")
                .setAllowedOrigins("*")//添加允许跨域访问
                .withSockJS() ;
    }

    /**
     * 配置消息代理
     * 启动Broker，消息的发送的地址符合配置的前缀来的消息才发送到这个broker
     */
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        //服务器发送给客户端的前缀，配置一个/topic广播消息代理和“/user”,"/queue"一对一消息代理
        registry.enableSimpleBroker("/user/", "/topic", "/queue", "/test","chat");

        //这个配置的是服务器订阅消息的前缀，比如@MessageMapping("/hello-socket")，浏览器端发送消息地址就是/app/hello-socket;
        registry.setApplicationDestinationPrefixes("/app");//全局使用的订阅前缀,应用请求前缀

        registry.setUserDestinationPrefix("/user");//点对点使用的订阅前缀（客户端订阅路径上会体现出来），不设置的话，默认也是/user/
    }

}
