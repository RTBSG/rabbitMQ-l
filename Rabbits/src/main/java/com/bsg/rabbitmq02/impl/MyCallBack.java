package com.bsg.rabbitmq02.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**实现功能需要实现对应的接口
 * 回调接口 RabbitTemplate.ConfirmCallback
 * 回退接口 RabbitTemplate.ReturnsCallback
 * @author bilib
 * @date 2021/08/06
 */
@Component
@Slf4j
public class MyCallBack implements RabbitTemplate.ConfirmCallback ,RabbitTemplate.ReturnsCallback {
@Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 当前类是自己实现的框架的接口 需要手动注入到框架提供RabbitTemplate
     * 初始化
     */
    @PostConstruct
    public void init() {
        rabbitTemplate.setConfirmCallback(this);
        rabbitTemplate.setReturnsCallback(this);
    }

    /**
     * 确认
     * 交换机确认回调方法
     * 调用时机
     * 1.发消息 交换机接收到消息 回调
     * 1.1 correlationData 保存回调消息的ID及相关信息
     * 1.2 交换机收到信息 ack=ture 判断成功或者失败
     * 1.3 cause null
     * 2.发消息 交换机未成功接收消息 回调
     * 2.1 correlationData  保存回调消息的ID及相关信息
     * 2.2 交换机收到消息 ack=false
     * 2.3 cause 失败的原因
     * @param correlationData 关联数据 参数来自生产者rabbitTemplate.convertAndSend的重载方法
     * @param ack             消
     * @param cause           导致
     */
    @Override
    public void confirm(CorrelationData correlationData, boolean ack, String cause) {

            String id=correlationData != null ? correlationData.getId() : "";
            if(ack){
                log.info("交换机已经收到 id 为:{}的消息",id);
            }else{
                log.info("交换机还未收到 id 为:{}消息,由于原因:{}",id,cause);
            }

        }

    /**
     * 返回消息
     *只有不可达目的地的时候才能回退
     * 现在已经封装到对象中了
     * @param returnedMessage 返回消息
     */
    @Override
    public void returnedMessage(ReturnedMessage returnedMessage) {
        log.info("消息:{}被服务器退回，退回原因:{}, 交换机是:{}, 路由  key:{}",
                new String(returnedMessage.getMessage().getBody()),
                returnedMessage.getReplyText(), returnedMessage.getExchange(),
                returnedMessage.getRoutingKey());
    }
}


