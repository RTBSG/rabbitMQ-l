package com.bsg.three;

import com.bsg.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * 生产商
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Consumer2 {
    /**
     * 交换机的名字
     */
    public static final String EXCHANGE_NAME = "logs";
    public static void main(String[] args) throws IOException, TimeoutException {

        Channel channel = Utils.getChannel();
//        声明一个交换机 fanout类型
        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");

        /**
         * 临时队列，队列的名称是随机的
         * 消费者断开连接后 队列自动删除
         */
//        声明一个临时队列
        String queueName=channel.queueDeclare().getQueue();
//        绑定交换机与队列
        channel.queueBind(queueName,EXCHANGE_NAME,"");
        System.out.println("等待接收消息，把接收的消息打印到控制台");
//        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)-> {
            System.out.println("消费者02控制台打印接收的消息:"+new String(message.getBody(),"UTF-8"));
        };
//        消费者取消消息回调接口 可以简单写

        channel.basicConsume(queueName,true,deliverCallback,consumerTag->{});


    }
}
