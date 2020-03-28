package com.example.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send {
    private final static String QUENE_NAME = "hello";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.20.24");
        factory.setUsername("root");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUENE_NAME, true, false, false, null);
        String message = "hello world!";
        channel.basicPublish("", QUENE_NAME, null, message.getBytes("UTF-8"));
        System.out.println("[x] send '" + message + "'");
    }
}
