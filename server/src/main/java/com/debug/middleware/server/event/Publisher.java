package com.debug.middleware.server.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tang on 2020/7/21
 */
//生产者
@Component
public class Publisher {

    private static final Logger log= LoggerFactory.getLogger(Publisher.class);

    //定义发送消息的组件
    @Autowired
    private ApplicationEventPublisher publisher;
    //发送消息的方法
    public void sendMsg() throws Exception{
        LoginEvent event=new LoginEvent(this,"Tang",new SimpleDateFormat("yyyyy-MM-dd HH:mm:ss").format(new Date()),"127.0.0.1");
        publisher.publishEvent(event);
        log.info("Spring事件驱动模型-发送消息：{}",event);
    }

}