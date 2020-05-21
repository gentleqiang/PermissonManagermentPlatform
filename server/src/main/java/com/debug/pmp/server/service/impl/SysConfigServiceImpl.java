package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysConfig;
import com.debug.pmp.model.mapper.SysConfigMapper;
import com.debug.pmp.server.service.SysConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("sysConfigService")
public class SysConfigServiceImpl extends ServiceImpl<SysConfigMapper,SysConfig> implements SysConfigService{

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public List<SysConfig> getAll() throws Exception {
        ListOperations<String,SysConfig> listOperations = redisTemplate.opsForList();
        List<SysConfig> list = listOperations.range(Constant.RedisSysConfigsKey, 0, listOperations.size(Constant.RedisSysConfigsKey));
        return list;
    }
}
