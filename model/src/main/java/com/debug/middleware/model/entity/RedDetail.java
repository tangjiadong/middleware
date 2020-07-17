package com.debug.middleware.model.entity;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Tang on 2020/7/16
 */
//红包随机金额明细
@Data
public class RedDetail {
    private Integer id;

    private Integer recordId;//红包记录id

    private BigDecimal amount;//红包随机金额

    private Byte isActive;

    private Date createTime;
}
