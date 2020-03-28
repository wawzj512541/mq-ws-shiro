package com.example.message.controller;

import com.example.message.entity.RequestMessage;
import com.example.message.entity.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @Description:
 * @Author: vesus
 * @CreateDate: 2018/5/29 下午1:48
 * @Version: 1.0
 */
@Controller
public class WebSocketController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate ;

    @ResponseBody
    @RequestMapping("/test/{userid}")
    public String test(@PathVariable("userid") String userid, RequestMessage msg){
        this.messagingTemplate.convertAndSendToUser(userid,"/message", msg.getMessage());//" /user/123/message "
        return "发送成功！";
    }

    //  "/app/welcome
    @MessageMapping("/welcome")
    public void toTopic(RequestMessage msg) throws Exception {
        System.out.println("======================"+msg.getMessage());
        this.messagingTemplate.convertAndSend("/api/v1/socket/send", msg.getMessage());

        this.messagingTemplate.convertAndSend("/topic/send", msg.getMessage());
    }

    @MessageMapping("/chat")
    public void toUser(RequestMessage msg) {

        String currentId = msg.getUserid().toString();

        System.out.println("----------------" + msg.getMessage());

        ResponseMessage message1 = new ResponseMessage(currentId, "我说：" + msg.getMessage());
        ResponseMessage message2 = new ResponseMessage(currentId, currentId + "说：" + msg.getMessage());

        // /user/{userId}/message
        this.messagingTemplate.convertAndSendToUser(msg.getUserid().toString(),"/chat", message1);
        this.messagingTemplate.convertAndSendToUser(msg.getToUserId().toString(),"/chat", message2);
    }
}
