package com.debug.middleware.server.dto;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotNull;

/**
 * Created by Tang on 2020/7/17
 */
//封装成实体对象
@Data
@ToString
public class RedPacketDto {
    private Integer userId;//用户id
    @NotNull(message = "红包个数不能为空")
    private Integer total;//红包个数
    @NotNull(message = "总金额不能为空")
    private Integer amount;//总金额,单位为分
}
