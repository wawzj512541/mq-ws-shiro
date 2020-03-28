package com.example.message.controller;

import com.example.message.entity.RequestMessage;
import com.example.message.entity.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 广播信息，凡是订阅了/topic/getResponse路径的信息都能接受到
 */
@Controller
public class TopicSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate ;

    /**
     * /app/welcomeTopic
     * @param message
     * @return
     * @throws Exception
     */
    @MessageMapping("/welcomeTopic")//浏览器发送请求通过@messageMapping 映射/welcome 这个地址。
    @SendTo("/topic")//服务器端有消息时,会订阅@SendTo 中的路径的浏览器发送消息。
    public ResponseMessage say(RequestMessage message) throws Exception {
        System.out.println("发送信息-----------------------" + message.getMessage());

        return new ResponseMessage("1","Welcome, " + message.getMessage() + "!");
    }

    @MessageMapping("/test")
    @SendTo("/test")
    public ResponseMessage test(String test){
        System.out.println(test);
        return new ResponseMessage("admin",test);
    }
}
