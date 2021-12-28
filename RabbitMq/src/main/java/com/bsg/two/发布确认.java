package com.bsg.two;

import com.bsg.Utils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmCallback;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeoutException;

/**
 * 发布确认
 *
 * @author bilib
 * @date 2021/08/04
 */
public class 发布确认 {
    public static final int MESSAGE_COUNT = 1000;

    public static void main(String[] args) throws Exception {
        pma();
    }

    public static void BatchRelease() throws Exception {
        try (Channel channel = Utils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            /**
             * 生成一个队列
             * 1.队列名称
             * 2.队列是否需要持久化（磁盘），默认false 队列在内存中，ture 写到硬盘
             * 3.该队列是否只供一个消费者消费，是否进行消息共享，true 多个消费者，false 单个消费者
             * 4.是否自动删除，最后一个消费者断开连接以后，该队列是否自动删除，true 自动删除，false不删除
             * 5.其他参数
             */
            channel.queueDeclare(queueName, false, false, false, null);
       /*    开启发布确认
      channel.confirmSelect();
      单个确认，发一条等一条然后发第二条 非常慢
      批量确认发布 先发布一批消息然后一起确认
      异步确认发布

      等待发布确认
      channel.waitForConfirms()

      */
            channel.confirmSelect();
//批量确认消息大小 100条 100条确认
            int batchSize = 100;

            long begin = System.currentTimeMillis();
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "";
                /** 队列创建完成向消费者发送信息
                 * 发送一个消费
                 * 1.发送到那个交换机
                 * 2.路由的key是那个，本次的队名名称
                 * 3.其他参数消息 消息持久化添加 MessageProperties.PERSISTENT_TEXT_PLAIN 这个属性。
                 * 4.发送消息的消息体
                 *
                 */
                channel.basicPublish("", queueName, null, message.getBytes());


                if (i % batchSize == 0) {
//                    确认 只有确认才会继续发
                    channel.waitForConfirms();

                }
            }

            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");
        }
    }


    /**
     * pma
     */
    public static void pma() throws Exception {
        try (Channel channel = Utils.getChannel()) {
            String queueName = UUID.randomUUID().toString();
            channel.queueDeclare(queueName, false, false, false, null);
//开启发布确认
            channel.confirmSelect();
//批量确认消息大小 100条 100条确认
            int batchSize = 100;

            /**ConcurrentLinkedQueue
             * 线程安全有序的跳表，适用于高并发的情况
             *轻松地将序号key与消息value进行关联
             * 轻松的批量删除条目，通过序号删除
             * 支持高并发 多线程
             *
             * 确认有多少未发布的信息有三步
             * 1.记录所有要发送的消息总和
             * 2.删除已经确认的消息，剩下的就是委未确认的消息
             * 3.打印未确认的消息
             */
            ConcurrentSkipListMap<Long, String> outstandingConfirms = new ConcurrentSkipListMap<>();

            long begin = System.currentTimeMillis();
            /**
             * 准备监听器，监听那些消息成功，那些失败
             *             消息确认成功回调函数
             */
            /**
             * 确认收到消息的一个回调
             * 1.消息序列号
             * 2.true 可以确认小于等于当前序列号的消息
             *   false 确认当前序列号消息
             */
            ConfirmCallback ackCallback = (deliveryTag, multiple) -> {
//                multiple 批量处理
                if (multiple) {
 //                2.删除已经确认的消息，剩下的就是委未确认的消息
                    ConcurrentNavigableMap<Long, String> confirms =
                            outstandingConfirms.headMap(deliveryTag);
                    confirms.clear();

                }else {
//                    如果不是批量,单个确认
                    outstandingConfirms.remove(deliveryTag);
                }
                System.out.println("确认的消息" + deliveryTag);
            };
            /**消息确认失败回调函数
             * 消息的标记
             * 是否为批量确认
             */
            ConfirmCallback nckCallback = (deliveryTag, multiple) -> {
//                3.打印未确认的消息
                String message = outstandingConfirms.get(deliveryTag);
                System.out.println("未确认的消息是"+message+"未确认的消息标记：" + deliveryTag);
            };
            /**
             * 添加一个异步确认的监听器
             * 1.确认收到消息的回调
             * 2.未收到消息的回调
             */

            channel.addConfirmListener(ackCallback, nckCallback);
//            批量发布
            for (int i = 0; i < MESSAGE_COUNT; i++) {
                String message = i + "消息";
                channel.basicPublish("", queueName, null, message.getBytes());
//                1.记录所有要发送的消息总和 序号在信道里面
                outstandingConfirms.put(channel.getNextPublishSeqNo(), message);
//无需确认，确认由交换机队列broker确认

            }
            long end = System.currentTimeMillis();
            System.out.println("发布" + MESSAGE_COUNT + "个批量确认消息,耗时" + (end - begin) + "ms");
        }
    }
}
