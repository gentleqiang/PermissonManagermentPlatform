package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysPostEntity;

import java.util.Map;

public interface SysPostService extends IService<SysPostEntity> {

    public PageUtil queryPage(Map<String,Object> paramMap);

    public void savePost(SysPostEntity sysPostEntity);

    public void updatePost(SysPostEntity sysPostEntity);

    public void deletePatch(Long[] ids);
}
