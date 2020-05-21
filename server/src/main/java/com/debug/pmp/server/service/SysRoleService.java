package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysRoleEntity;

import java.util.List;
import java.util.Map;

/**
 * @author gentleman_qiang
 */
public interface SysRoleService extends IService<SysRoleEntity> {

    PageUtil querypage(Map<String,Object> params);

    void saveRole(SysRoleEntity sysRoleEntity);

    SysRoleEntity queryByRoleId(Long roleId);

    void updateRole(SysRoleEntity sysRoleEntity);

    void deleteBatch(Long[] roleId);

    List<String> queryRolename();
}
