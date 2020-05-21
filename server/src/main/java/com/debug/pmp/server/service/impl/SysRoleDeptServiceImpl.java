package com.debug.pmp.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.CommonUtil;
import com.debug.pmp.model.entity.SysRoleDeptEntity;
import com.debug.pmp.model.entity.SysRoleMenuEntity;
import com.debug.pmp.model.mapper.SysRoleDeptDao;
import com.debug.pmp.model.mapper.SysRoleMenuDao;
import com.debug.pmp.server.service.SysRoleDeptService;
import com.debug.pmp.server.service.SysRoleMenuService;
import com.google.common.base.Joiner;
import net.sf.jsqlparser.statement.select.Join;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author gentlemanz_qiang
 */
@Service("sysRoleDeptService")
public class SysRoleDeptServiceImpl extends ServiceImpl<SysRoleDeptDao, SysRoleDeptEntity> implements SysRoleDeptService {


    /**
     * 添加角色信息
     * @param roleId
     * @param deptIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveOrUpdate(Long roleId, List<Long> deptIdList) {
        deleteBatch(Arrays.asList(roleId));

        SysRoleDeptEntity sysRoleDeptEntity;

        for (Long deptId : deptIdList) {
            sysRoleDeptEntity = new SysRoleDeptEntity();

            sysRoleDeptEntity.setRoleId(roleId);
            sysRoleDeptEntity.setDeptId(deptId);

            this.save(sysRoleDeptEntity);
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
        return baseMapper.queryDeptIdListByRoleId(roleId);
    }
}
