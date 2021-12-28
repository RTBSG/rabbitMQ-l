package com.bsg.six;

import com.bsg.Utils;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

/**
 * consumer01
 *只处理死信队列
 * @author bilib
 * @date 2021/08/05
 */
public class Consumer02 {

    public static final String DEAD_QUEUE = "dead_queue";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();

        //        声明接收消息
        DeliverCallback deliverCallback =(consumerTag, message)-> {
            System.out.println("consumer02:"+new String(message.getBody(),"UTF-8"));
        };

        channel.basicConsume(DEAD_QUEUE, true, deliverCallback, consumerTag -> { });

    }



}
