package com.debug.middleware.api.response;

import com.debug.middleware.api.enums.StatusCode;

/**
 * Created by Tang on 2020/7/17
 */
public class BaseResponse<T> {

    private Integer code;//状态码
    private String msg;//描述信息
    private T data;//响应数据,采用泛型标识可以接受通用的数据类型

    public BaseResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public BaseResponse(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public BaseResponse(StatusCode statusCode) {
        this.code = statusCode.getCode();
        this.msg = statusCode.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
