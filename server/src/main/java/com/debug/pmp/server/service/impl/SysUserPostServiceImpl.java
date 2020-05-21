package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.CommonUtil;
import com.debug.pmp.model.entity.SysUserPostEntity;
import com.debug.pmp.model.mapper.SysUserPostDao;
import com.debug.pmp.server.service.SysUserPostService;
import com.google.common.base.Joiner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gentleman_qiang
 */
@Service("sysUserPostService")
public class SysUserPostServiceImpl extends ServiceImpl<SysUserPostDao, SysUserPostEntity> implements SysUserPostService {

    private final static Logger log = LoggerFactory.getLogger(SysUserPostServiceImpl.class);
    /**
     * 根据用户Id获取岗位
     * @param userId
     * @return
     */
    @Override
    public String getPostNamesByUserId(Long userId) {
        /*Set<String> postName = baseMapper.getPostNamesByUserId(userId);
        if(postName != null && postName.size() > 0){
            return Joiner.on(",").join(postName);
        }else{
            return "";
        }*/

        //第二种写法
        List<SysUserPostEntity> byUserId = baseMapper.getByUserId(userId);
        String result = null;
        if(byUserId != null && !byUserId.isEmpty()){
            Set<String> collect = byUserId.stream().map(SysUserPostEntity::getPostName).collect(Collectors.toSet());
            result = Joiner.on(",").join(collect);
        }
        return result;
    }

    /**
     * 添加用户，维护用户-岗位表
     * @param userId
     * @param postIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void savePost(Long userId, List<Long> postIdList) {

        this.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",userId));
        SysUserPostEntity entity;
        for (Long postId : postIdList) {
            entity = new SysUserPostEntity();
            entity.setUserId(userId);
            entity.setPostId(postId);
            this.save(entity);
        }

    }

    /**
     * 根据用户id查 岗位
     * @param userId
     * @return
     */
    @Override
    public List<Long> queryPostIdList(Long userId) {
        return baseMapper.queryPostIdList(userId);
    }

    /**
     * 清除角色的同时，清除用户岗位的信息
     * @param postId
     */
    @Override
    public void deleteBatch(List<Long> postId) {
        String delIds = Joiner.on(",").join(postId);
        baseMapper.deleteBatch(CommonUtil.concatStrToChar(delIds,","));
    }
}
