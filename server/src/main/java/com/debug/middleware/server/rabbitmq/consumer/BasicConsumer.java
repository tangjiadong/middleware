package com.debug.middleware.server.rabbitmq.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * 基本消息模型(消费者)
 * Created by Tang on 2020/7/23
 */
@Component
@Slf4j
public class BasicConsumer {
    //定义JSON序列化和反序列实例
    @Autowired
    private ObjectMapper objectMapper;

    @RabbitListener(queues = "${mq.basic.info.queue.name}", containerFactory = "singleListenerContainer")
    public void consumeMsg(@Payload byte[] msg) {
        try {
            //将字节数组的消息转换为字符串并输出
            String message = new String(msg,"utf-8");
            //输出日志
            log.info("基本消息模型-生产者-监听消费消息:{}",message);
        } catch (Exception e) {
            log.error("基本消息模型-生产者-发生异常:{}",e.fillInStackTrace());
        }

    }
}
