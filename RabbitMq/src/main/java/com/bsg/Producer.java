package com.bsg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产商
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Producer {
    /**
     * 队列名称
     */
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
 * 生成一个队列
 * 1.队列名称
 * 2.队列是否需要持久化（磁盘），默认false 队列在内存中，ture 写到硬盘
 * 3.该队列是否只供一个消费者消费，是否进行消息共享，true 多个消费者，false 单个消费者
 * 4.是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true 自动删除，false不删除
 * 5.其他参数
 */
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        String message = "hello word rbbitmq";
        /**
         * 发送一个消费
         * 1.发送到那个交换机
         * 2.路由的key是那个，本次的队名名称
         * 3.其他参数消息
         * 4.发送消息的消息体
         *
         */
        channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
        System.out.println("消息发送完毕");
    }
}
