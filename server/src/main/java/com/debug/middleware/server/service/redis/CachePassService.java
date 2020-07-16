package com.debug.middleware.server.service.redis;

import com.debug.middleware.model.entity.Item;
import com.debug.middleware.model.mapper.ItemMapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.assertj.core.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tang on 2020/7/16
 */
//缓存穿透
@Service
public class CachePassService {
    private static final Logger log = LoggerFactory.getLogger(CachePassService.class);

    //定义Mapper
    @Autowired
    private ItemMapper itemMapper;
    //定义Redis操作组件
    @Autowired
    private RedisTemplate redisTemplate;
    //定义JSON序列化与反序列化框架类
    @Autowired
    private ObjectMapper objectMapper;
    //定义缓存中Key命名的前缀
    private static final String keyPrefix = "item:";

    /**
     * 获取商品详情
     * 如果缓存有,则从缓存中获取;如果没有,则总数据库查询,并将查询结构放入缓存中
     *
     * @param itemCode
     * @return
     */
    public Item getItemInfo(String itemCode) throws IOException {
        //定义商品对象
        Item item = null;
        //定义缓存中真正的Key:由前缀和商品编码组成
        final String key = keyPrefix + itemCode;
        log.info("service进入--------->>>>>");
        //定义Redis的操作组件ValueOperations
        ValueOperations valueOperations = redisTemplate.opsForValue();
        if (redisTemplate.hasKey(key)) {
            log.info("---获取商品详情-缓存中存在该商品----商品编号为:{}", itemCode);
            //从缓存中查询该商品详情
            Object res = valueOperations.get(key);
            if (res != null && !Strings.isNullOrEmpty(res.toString())) {
                //如果可以找到该商品,则进行JSON反序列化解析
                item = objectMapper.readValue(res.toString(), Item.class);
            }
        } else {
            //在缓存中没有找到该商品
            log.info("---获取商品详情-缓存中不存在该商品-从数据库中查询---商品编号为:{}", itemCode);
            //从数据库中获取该商品详情
            item = itemMapper.selectByCode(itemCode);
            if (item != null) {
                //如果数据库中查询到该商品,则将其序列化后并写入缓存中
                valueOperations.set(key, objectMapper.writeValueAsString(item));
            } else {
                //过期失效时间设置为30min,根据业务决定
                valueOperations.set(key, "", 30L, TimeUnit.MINUTES);
            }
        }
        return item;
    }

}
