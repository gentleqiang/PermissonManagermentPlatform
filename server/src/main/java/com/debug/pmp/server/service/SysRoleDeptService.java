package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysRoleDeptEntity;
import com.debug.pmp.model.entity.SysRoleMenuEntity;

import java.util.List;

/**
 * @author gentleman_qiang
 */
public interface SysRoleDeptService extends IService<SysRoleDeptEntity> {
    void saveOrUpdate(Long roleId, List<Long> deptIdList);
    void deleteBatch(List<Long> roleIds);
    List<Long> queryByRoleId(Long roleId);
}
