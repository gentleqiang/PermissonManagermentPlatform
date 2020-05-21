package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysUserRoleEntity;

import java.util.List;

/**
 * @author Administrator
 */
public interface SysUserRoleService extends IService<SysUserRoleEntity> {

    void deleteBatch(List<Long> roleId);

    void saveRole(Long userId, List<Long> roleIdList);

    List<Long> queryRoldIdList(Long userId);
}
