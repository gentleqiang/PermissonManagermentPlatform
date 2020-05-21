package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysUserPostEntity;

import java.util.List;
import java.util.Set;

/**
 * @author gentleman_qiang
 */
public interface SysUserPostService extends IService<SysUserPostEntity>{

    //根据用户id查岗位姓名
    String getPostNamesByUserId(Long userId);

    void savePost(Long userId, List<Long> postIdList);

    List<Long> queryPostIdList(Long userId);

    void deleteBatch(List<Long> postId);
}
