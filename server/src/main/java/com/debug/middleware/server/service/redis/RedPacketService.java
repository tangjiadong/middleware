package com.debug.middleware.server.service.redis;

import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.service.IRedPacketService;
import com.debug.middleware.server.service.IRedService;
import com.debug.middleware.server.util.RedPacketUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tang on 2020/7/17
 */
@Slf4j
@Service
public class RedPacketService implements IRedPacketService {
    //存储至缓存系统Redis时定义的key前缀
    private static final String keyPrefix = "redis:red:packet:";
    //定义Redis操作Bean组件
    @Autowired
    private RedisTemplate redisTemplate;
    //自动注入红包业务逻辑处理过程数据记录接口服务
    @Autowired
    private IRedService redService;

    /**
     * 发红包
     *
     * @param dto
     * @return
     * @throws Exception
     */
    @Override
    public String handOut(RedPacketDto dto) throws Exception {
        if (dto.getTotal() > 0 && dto.getAmount() > 0) {
            //采用二倍均值法生成随机金额列表
            List<Integer> list = RedPacketUtil.divideRedPackage(dto.getAmount(), dto.getTotal());
            //生成红包全局唯一标识串
            String timestamp = String.valueOf(System.nanoTime());
            //根据缓存key的前缀与其他信息拼接成一个新的用于存储随机金额列表的Key
            String redId = new StringBuffer(keyPrefix).append(dto.getUserId()).append(":").append(timestamp).toString();
            //将随机金额列表存入缓存中(leftPushAll)
            redisTemplate.opsForList().leftPushAll(redId, list);

            //根据缓存key的前缀与其他信息拼接成一个新的用于存储红包总数的Key
            String redTotalKey = redId + ":total";
            //将红包总数存入缓存中
            redisTemplate.opsForValue().set(redTotalKey, dto.getTotal());

            //异步记录红包的全局唯一标识串,红包个数与随机金额列表进入数据库
            redService.recordRedPacket(dto, redId, list);
            //将红包的全局唯一标识串返回给前端
            return redId;
        } else {
            throw new Exception("系统异常-分发红包-参数不合法!");
        }

    }

    /**
     * 抢红包
     *
     * @param userId
     * @param redId
     * @return
     * @throws Exception
     */
    @Override
    public BigDecimal rob(Integer userId, String redId) throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //在处理用户抢红包之前,先判断一下当前用户是否已经抢过该红包
        //如果已经抢过了,则直接返回红包金额,并在前端显示出来
        Object obj = valueOperations.get(redId + userId + ":rob");
        if (obj != null) {
            return new BigDecimal(obj.toString());
        }
        //"点红包"业务逻辑,主要用于判断缓存系统中是否仍然有红包,即红包剩余个数是否大于0
        Boolean res = click(redId);
        if (res) {
            //res为true,则可以进入"抢红包"业务逻辑的处理
            //首先是从小红包随机金额列表中弹出一个随机金额
            Object value = redisTemplate.opsForList().rightPop(redId);
            if (value != null) {
                //value不为空,即红包金额不为0,进而表示当前用户抢到一个红包,则可以进入后续的更新缓存与记录信息入数据库
                String redTotalKey = redId + ":total";
                //更新缓存中剩余的红包数,即红包个数减1
                Integer currTotal = valueOperations.get(redTotalKey) != null ? (Integer) valueOperations.get(redTotalKey) : 0;
                valueOperations.set(redTotalKey, currTotal - 1);
                //将红包金额返回给用户前,金额的单位设置为"元"
                BigDecimal result = new BigDecimal(value.toString()).divide(new BigDecimal(100));
                //记录抢到红包时用户的账号信息以及抢到的金额等信息入数据库
                redService.recordRobRedPacket(userId, redId, new BigDecimal(value.toString()));
                //将当前抢到红包的用户信息放置进缓存系统中,用于表示用户已经抢过
                //TTL为24小时
                valueOperations.set(redId + userId + ":rob", result, 24L, TimeUnit.HOURS);
                //输出当前用户抢到红包的记录信息
                log.info("当前用户抢到红包了:userId={} key{} 金额={} ", userId, redId, result);
                return result;
            }
        }
        //null表示当前用户没有抢到红包
        return null;
    }

    /**
     * 点红包
     * true:缓存系统Redis还有红包,即红包剩余个数大于0
     * false:红包被抢光了
     *
     * @param redId
     * @return
     * @throws Exception
     */
    private Boolean click(String redId) throws Exception {
        ValueOperations valueOperations = redisTemplate.opsForValue();
        //定义用于查询缓存系统中红包剩余个数的key
        String redTotalKey = redId + ":total";
        //获取缓存系统中Redis中剩余的红包数
        Object total = valueOperations.get(redTotalKey);
        //个数判断
        if (total != null && Integer.valueOf(total.toString()) > 0) {
            return true;
        }
        //返回false,则表示已经没有红包可抢了
        return false;
    }

}
