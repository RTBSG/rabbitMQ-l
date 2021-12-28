package com.bsg;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 消费者
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Consumer {
    public static final String QUEUE_NAME = "hello";

    public static void main(String[] args) throws IOException, TimeoutException {
        //创建一个连接工厂
        ConnectionFactory factory=new ConnectionFactory();
//        工厂ip 连接rabbitmq的队列
        factory.setHost("192.168.5.129");
//        用户名和密码
        factory.setUsername("admin");
        factory.setPassword("151385568300");
//        创建连接
        Connection connection = factory.newConnection();
//        创建信道
        Channel channel=connection.createChannel();

        /**
         * 消费者接收消息
         * 1.消费那个队列
         * 2.消费成功之后是否自动应答 true 代表自动应答，false 手动应答
         * 3.消费者未成功接收的回调函数
         * 4.消费者成功接收的回调函数
         */
//        声明接收消息
        DeliverCallback deliverCallback =(consumerTag,message)->{
//        接收的是对象的地址 com.rabbitmq.client.Delivery@6a11c0fd
            System.out.println(message);
//            接收消息体
            System.out.println(new String(message.getBody()));
        };
//       取消消息的回调
        CancelCallback cancelCallback =consumerTag->{

            System.out.println("消费消息中断了");
        };


        channel.basicConsume(QUEUE_NAME, true, deliverCallback, cancelCallback);


    }
}
