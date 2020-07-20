package com.debug.middleware.server.service.redis;

import com.debug.middleware.model.entity.RedDetail;
import com.debug.middleware.model.entity.RedRecord;
import com.debug.middleware.model.entity.RedRobRecord;
import com.debug.middleware.model.mapper.RedDetailMapper;
import com.debug.middleware.model.mapper.RedRecordMapper;
import com.debug.middleware.model.mapper.RedRobRecordMapper;
import com.debug.middleware.server.dto.RedPacketDto;
import com.debug.middleware.server.service.IRedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by Tang on 2020/7/17
 */
//红包业务逻辑处理过程数据记录接口,异步实现
@Service
@EnableAsync
@Slf4j
public class RedService implements IRedService {

    @Autowired
    private RedRecordMapper redRecordMapper;
    @Autowired
    private RedDetailMapper redDetailMapper;
    @Autowired
    private RedRobRecordMapper redRobRecordMapper;

    /**
     * 记录发红包时红包的全局唯一标识串,随机金额列表和红包个数等信息入数据库
     * @param dto  红包总金额+个数
     * @param redId  红包全局唯一标识串
     * @param list 红包随机金额列表
     * @throws Exception
     */
    @Override
    @Async
    @Transactional(rollbackFor = Exception.class)
    public void recordRedPacket(RedPacketDto dto, String redId, List<Integer> list) throws Exception {
        //定义实体类对象
        RedRecord redRecord = new RedRecord();
        //设置字段的取值信息
        redRecord.setUserId(dto.getUserId());
        redRecord.setRedPacket(redId);
        redRecord.setTotal(dto.getTotal());
        redRecord.setAmount(BigDecimal.valueOf(dto.getAmount()));
        redRecord.setCreateTime(new Date());
        //将对象信息插入数据库
        redRecordMapper.insertSelective(redRecord);
        //定义红包随机金额明细实体类对象
        RedDetail detail;
        //遍历随机金额列表,将金额等信息设置到相应的字段中
        for (Integer i : list) {
            detail = new RedDetail();
            detail.setRecordId(redRecord.getId());
            detail.setAmount(BigDecimal.valueOf(i));
            detail.setCreateTime(new Date());
            //将对象信息插入数据库
            redDetailMapper.insertSelective(detail);
        }
    }

    /**
     * 记录抢红包用户抢到的红包金额等信息入数据库
     * @param userId
     * @param redId
     * @param amount
     * @throws Exception
     */
    @Override
    @Async
    public void recordRobRedPacket(Integer userId, String redId, BigDecimal amount) throws Exception {
        RedRobRecord redRobRecord = new RedRobRecord();
        redRobRecord.setUserId(userId);
        redRobRecord.setRedPacket(redId);//红包全局唯一标识串
        redRobRecord.setAmount(amount);//设置抢到的红包金额
        redRobRecord.setRobTime(new Date());//抢到的时间
        //将实体对象信息插入到数据库中
        redRobRecordMapper.insertSelective(redRobRecord);

    }
}
