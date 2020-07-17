package com.debug.middleware.server.service;

import com.debug.middleware.server.dto.RedPacketDto;

import java.math.BigDecimal;
import java.util.List;

/**
 * 红包业务逻辑处理过程数据记录接口,异步同步
 */
public interface IRedService {
    //记录发红包时红包的全局唯一标识串,随机金额列表和红包个数等信息入数据库
    void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception;
    //记录抢红包用户抢到的红包金额等信息入数据库
    void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception;
}
