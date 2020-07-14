package com.debug.middleware.server.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

/**
 * 自定义注入配置操作组件RedisTemplate和StringRedisTemplate
 */
//通用化配置
@Configuration
public class CommonConfig {
    //Redis链接工厂
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;
    //缓存操作组件RedisTemplate的自定义配置
    @Bean
    public RedisTemplate<String,Object> redisTemplate(){
        //定义RedisTemplate实例
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String,Object>();
        //设置Redis链接工厂
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        //指定大Key序列化策略为String序列化,value为JDK自带的序列化策略
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        //指定HashKey序列化策略为String序列化(针对Hash存储)
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        return redisTemplate;
    }

    //缓存操作组件StringRedisTemplate
    @Bean
    public StringRedisTemplate stringRedisTemplate(){
        //采用默认配置即可
        StringRedisTemplate stringRedisTemplate = new StringRedisTemplate();
        stringRedisTemplate.setConnectionFactory(redisConnectionFactory);
        return stringRedisTemplate;
    }
}
