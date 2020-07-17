package com.debug.middleware.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tang on 2020/7/16
 */
//发红包记录
@Data
public class RedRecord {
    private Integer id;

    private Integer userId;

    private String redPacket;//红包全局唯一id

    private Integer total;//红包指定可以抢的总人数

    private BigDecimal amount;//红包总金额

    private Byte isActive;//是否有效

    private Date createTime;
}
