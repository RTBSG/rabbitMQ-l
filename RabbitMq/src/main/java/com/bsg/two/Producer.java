package com.bsg.two;

import com.bsg.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;

/**
 * 生产商
 *
 * @author bilib
 * @date 2021/08/04
 */
public class Producer {
//    注意队列需要持久化的话，单单更改持久化选项是不行的，需要去rabbitmq删除原队列重新创建或者直接重新创建队列

    public static final String QUERY_NAME = "duileceshi";


    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Channel channel= Utils.getChannel();
        /**
         * 生成一个队列
         * 1.队列名称
         * 2.队列是否需要持久化（磁盘），默认false 队列在内存中，ture 写到硬盘
         * 3.该队列是否只供一个消费者消费，是否进行消息共享，true 多个消费者，false 单个消费者
         * 4.是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true 自动删除，false不删除
         * 5.其他参数
         */
        channel.queueDeclare(QUERY_NAME, true, false, false, null);
/*    开启发布确认
      channel.confirmSelect();

      单个确认，发一条等一条然后发第二条 非常慢
      批量确认发布 先发布一批消息然后一起确认
      异步确认发布

      等待发布确认
      channel.waitForConfirms()

      */
        channel.confirmSelect();
        //        从控制台接受信息
       Scanner scanner=new Scanner(System.in);
        System.out.println("请输入信息");
       while (scanner.hasNext()) {
            String message=scanner.next();
           /** 队列创建完成向消费者发送信息
            * 发送一个消费
            * 1.发送到那个交换机
            * 2.路由的key是那个，本次的队名名称
            * 3.其他参数消息 消息持久化添加 MessageProperties.PERSISTENT_TEXT_PLAIN 这个属性。
            * 4.发送消息的消息体
            *
            */
           channel.basicPublish("", QUERY_NAME, MessageProperties.PERSISTENT_TEXT_PLAIN, message.getBytes());
           boolean flag=channel.waitForConfirms();

           if (flag) {
               System.out.println("消息发送完成");
           }
        }


    }


}
