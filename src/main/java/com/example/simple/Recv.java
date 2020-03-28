package com.example.simple;

import com.rabbitmq.client.*;

import java.io.IOException;

public class Recv {
    private final static String QUEUE_NAME = "hello";

    public static void main(String[] args) throws Exception{

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.20.24");
        factory.setUsername("root");
        factory.setPassword("123456");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        DeliverCallback deliverCallback = new DeliverCallback() {
            @Override
            public void handle(String s, Delivery delivery) throws IOException {
                String message = new String(delivery.getBody(),"UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME,true,deliverCallback, new CancelCallback() {
            @Override
            public void handle(String s) throws IOException {

            }
        });
    }
}
