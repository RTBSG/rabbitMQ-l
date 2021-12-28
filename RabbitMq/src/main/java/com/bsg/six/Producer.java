package com.bsg.six;

import com.bsg.Utils;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * 生产商
 *声明交换机的时候 生产者和消费者只需要有一个声明即可
 * @author bilib
 * @date 2021/08/05
 */
public class Producer {
    public static final String NORMAL_EXCHANGE = "normal_exchange";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {
        Channel channel = Utils.getChannel();
/*    死信消息 设置ttl时间 在时间内没有接受转为死信队列
        AMQP.BasicProperties properties = new AMQP.BasicProperties().builder()
                .expiration("1000").build();
*/

        for (int i = 0; i < 20; i++) {

            String message = "info" + i;
            /**
             * 发送一个消费
             * 1.发送到那个交换机
             * 2.路由的key是那个，本次的队名名称
             * 3.其他参数消息
             * 4.发送消息的消息体
             *
             */
            channel.basicPublish(NORMAL_EXCHANGE, "zhangsan",  null,message.getBytes("UTF-8"));
            System.out.println("生产者发出消息"+message);
        }

        }



    }
