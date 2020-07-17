package com.debug.middleware.server.controller.redis;

import com.debug.middleware.api.enums.StatusCode;
import com.debug.middleware.api.response.BaseResponse;
import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.service.IRedPacketService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Tang on 2020/7/17
 */
//红包处理逻辑
@RestController
@Slf4j
@RequestMapping("/red/packet")
public class RedPacketController {
    //注入红包业务逻辑处理接口服务
    @Autowired
    private IRedPacketService redPacketService;

    /**
     * 发红包请求,请求方式为POST,请求参数采用JSON格式进行提交
     * @param dto
     * @param result
     * @return
     */
    @RequestMapping(value = "/hand/out",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse handOut(@Validated @RequestBody RedPacketDto dto, BindingResult result){
        //参数校验
        if (result.hasErrors()){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //核心业务逻辑处理服务,最终返回红包全局唯一标识串
            String redId = redPacketService.handOut(dto);
            //将红包全局唯一标识串返回前端
            response.setData(redId);
        } catch (Exception e) {
            //如果报异常则输出日志并返回相应的错误信息
            log.error("红包发生异常:dto={}",dto,e.fillInStackTrace());
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}
