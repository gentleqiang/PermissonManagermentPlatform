package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysRoleMenuEntity;
import org.apache.poi.ss.formula.functions.IDStarAlgorithm;

import java.util.List;

/**
 * @author Administrator
 */
public interface SysRoleMenuService extends IService<SysRoleMenuEntity> {

    void saveOrUpdate(Long roleId, List<Long> menuIdList);

    void deleteBatch(List<Long> roleIds);

    List<Long> queryByRoleId(Long roleId);
}
