package com.bsg.five;

import com.bsg.Utils;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 发出日志
 *
 * @author bilib
 * @date 2021/08/04
 */
public class DirectLogs {
    public static final String EXCHANGE_NAME = "topic_log";

    public static void main(String[] args) throws IOException, TimeoutException {
        Channel channel = Utils.getChannel();
        Map<String, String> map = new HashMap();
        map.put("quick.orange.fox", "被队列  Q1 接收到");
        map.put("lazy.brown.fox", "被队列 Q2 接收到");
        map.put("lazy.orange.elephant", "被队列 Q1Q2 接收到");
        map.put("quick.orange.rabbit", "被队列 Q1Q2 接收到");
//        System.out.println("请输入:");
//        Scanner scanner = new Scanner(System.in);
//        while (scanner.hasNext()) {
//            String message=scanner.next();
//
//        }
        /**
         * Map.Entry 他的用途是表示一个映射项（里面有Key和Value），
         * 而Set<Map.Entry<K,V>>表示一个映射项的Set。
         *
         * map.entrySet()
         * 返回此映射中包含的映射的 Set 视图。
         * [1=Google, 2=Runoob, 3=Taobao]
         */
        for (Map.Entry<String, String> key : map.entrySet()) {
           String routingKey= key.getKey();
            String message = key.getValue();
            /** 队列创建完成向消费者发送信息
             * 发送一个消费
             * 1.发送到那个交换机
             * 2.路由的key是那个，绑定规则
             * 3.其他参数消息 消息持久化添加 MessageProperties.PERSISTENT_TEXT_PLAIN 这个属性。
             * 4.发送消息的消息体
             */
            channel.basicPublish(EXCHANGE_NAME, routingKey, null, message.getBytes("UTF-8"));
            System.out.println("生产者发出消息"+message);
        }



    }
}
