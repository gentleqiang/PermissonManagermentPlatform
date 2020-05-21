package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysDeptEntity;
import com.debug.pmp.model.entity.SysMenuEntity;
import com.debug.pmp.model.entity.SysUserEntity;

import java.util.List;
import java.util.Map;

/**
 * @author gentleman_qiang
 */
public interface SysDeptService extends IService<SysDeptEntity> {

    List<SysDeptEntity> queryAll(Map<String,Object> paramMap);

    List<Long> queryDeptIds(Long deptId);

    void saveDept(SysUserEntity sysUserEntity);




}
