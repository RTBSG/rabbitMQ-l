package com.bsg;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 跑龙套
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Utils {
    public static Channel getChannel() throws IOException, TimeoutException {
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
        return channel;

    }

}
