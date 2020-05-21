package com.debug.pmp.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.SysDeptEntity;
import com.debug.pmp.model.entity.SysMenuEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.SysDeptDao;
import com.debug.pmp.server.service.SysDeptService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @author gentleman_qiang
 */
@Service("sysDeptServiceImpl")
public class SysDeptServiceImpl extends ServiceImpl<SysDeptDao,SysDeptEntity> implements SysDeptService {

    @Override
    public List<SysDeptEntity> queryAll(Map<String, Object> paramMap) {
        return baseMapper.queryList(paramMap);
    }

    /**
     * 查询当前部门是否有子部门
     * @param deptId
     * @return
     */
    @Override
    public List<Long> queryDeptIds(Long deptId) {
        return baseMapper.queryDeptIds(deptId);
    }

    /**
     * 添加用户是，关联的部门名称
     * @param sysUserEntity
     */
    @Override
    public void saveDept(SysUserEntity sysUserEntity) {
        SysDeptEntity entity = new SysDeptEntity();
        entity.setDeptId(sysUserEntity.getDeptId());
        entity.setName(sysUserEntity.getDeptName());
        this.save(entity);
    }
}
