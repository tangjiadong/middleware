package com.debug.middleware.model.entity;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.ToString;

import java.io.Serializable;
import java.util.*;

/**
*
*  @author Tang
*/
@Data
@ToString
public class Item implements Serializable {

    private static final long serialVersionUID = 1594864179174L;

    private Integer id;
    private String code;
    private String name;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

}
