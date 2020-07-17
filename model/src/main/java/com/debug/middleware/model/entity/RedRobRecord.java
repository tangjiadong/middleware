package com.debug.middleware.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tang on 2020/7/16
 */
//抢到红包时金额等相关信息
@Data
public class RedRobRecord {
    private Integer id;

    private Integer userId;

    private String redPacket;//红包全局唯一标识串

    private BigDecimal amount;//抢到的红包金额

    private Date robTime;//抢到时间

    private Byte isActive;//是否有效
}
