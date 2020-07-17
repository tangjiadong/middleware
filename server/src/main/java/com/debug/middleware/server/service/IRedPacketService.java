package com.debug.middleware.server.service;

import com.debug.middleware.server.dto.RedPacketDto;

import java.math.BigDecimal;

/**
 * 红包业务逻辑处理接口
 */
public interface IRedPacketService {
    //发红包核心业务逻辑的实现
    String handOut(RedPacketDto dto) throws Exception;
    //抢红包
    BigDecimal rob(Integer userId,String redId) throws Exception;
}
