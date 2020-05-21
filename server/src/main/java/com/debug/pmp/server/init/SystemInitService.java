package com.debug.pmp.server.init;

import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysConfig;
import com.debug.pmp.model.mapper.SysConfigMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 项目启动之后初始化一些类操作
 * @author Administrator
 */
@Component
public class SystemInitService implements CommandLineRunner {

    private static Logger log = LoggerFactory.getLogger(SystemInitService.class);

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void run(String... args) throws Exception {
        log.info("----项目启动之后初始化的操作----");
        init();
    }

    public void init(){
        try {


            log.info("----项目启动之后初始化的操作:开始----");
            /* 方式一
            List<SysConfig> list = sysConfigMapper.selectAll();
            redisTemplate.delete(Constant.RedisSysConfigsKey);
            redisTemplate.opsForList().leftPushAll(Constant.RedisSysConfigsKey,list);*/

            /*
            *  方式二：某产品经理突然下架所有系统编码类表，如何在不重启项目的前提下，无缝，平滑的实现前段
            *  业务的系统“系统编码列表”的自动更新呢
            * */
            List<SysConfig> list = sysConfigMapper.selectAll();
            ListOperations<String,SysConfig> operations = redisTemplate.opsForList();
            Long redisLength = operations.size(Constant.RedisSysConfigsKey);
            if(redisLength > 0){
                if(list != null && !list.isEmpty()){
                     //TODO 缓存里有，数据库也有，删除缓存，将数据库中的数据更新到缓存
                       redisTemplate.delete(Constant.RedisSysConfigsKey);
                       operations.leftPushAll(Constant.RedisSysConfigsKey,list);
                }else {
                    //TODO 缓存里有，数据库中没有，删除缓存里面的，同数据库保持一致
                    redisTemplate.delete(Constant.RedisSysConfigsKey);
                }
            }else{
                if(list != null && !list.isEmpty()){
                   //TODO 缓存没有，数据有， 则将数据库中的东西更新到缓存中，保持一致
                    operations.leftPushAll(Constant.RedisSysConfigsKey,list);
                }else {
                    //TODO 两者都没有，那就啥也没有
                }
            }

        }catch (Exception e){
            log.info("----项目启动之后初始化的操作:失败----",e.fillInStackTrace());
        }
    }
}
