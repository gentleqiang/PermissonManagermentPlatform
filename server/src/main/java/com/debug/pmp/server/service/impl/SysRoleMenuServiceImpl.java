package com.debug.pmp.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.CommonUtil;
import com.debug.pmp.model.entity.SysRoleMenuEntity;
import com.debug.pmp.model.mapper.SysRoleMenuDao;
import com.debug.pmp.server.service.SysRoleMenuService;
import com.google.common.base.Joiner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

@Service("sysRoleMenuService")
public class SysRoleMenuServiceImpl extends ServiceImpl<SysRoleMenuDao, SysRoleMenuEntity> implements SysRoleMenuService {


    /**
     * 添加信息
     * @param roleId
     * @param menuIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> menuIdList) {
         deleteBatch(Arrays.asList(roleId));

         SysRoleMenuEntity sysRoleMenuEntity;
        for (Long menuId : menuIdList) {
            sysRoleMenuEntity = new SysRoleMenuEntity();

            sysRoleMenuEntity.setRoleId(roleId);
            sysRoleMenuEntity.setMenuId(menuId);
            this.save(sysRoleMenuEntity);
        }
    }

    /**
     * 清楚缓存信息
     * @param roleIds
     */
    @Override
    public void deleteBatch(List<Long> roleIds) {
        if(roleIds != null && !roleIds.isEmpty()){
            String delIds = Joiner.on(",").join(roleIds);
            baseMapper.deleteBatch(CommonUtil.concatStrToInt(delIds,","));
        }
    }

    /**
     * 获取角色~部门详情
     * @param roleId
     * @return
     */
    @Override
    public List<Long> queryByRoleId(Long roleId) {
        return baseMapper.queryMenuIdList(roleId);
    }
}
