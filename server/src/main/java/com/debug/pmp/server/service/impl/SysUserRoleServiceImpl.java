package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.additional.query.impl.QueryChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.CommonUtil;
import com.debug.pmp.model.entity.SysRoleEntity;
import com.debug.pmp.model.entity.SysUserRoleEntity;
import com.debug.pmp.model.mapper.SysUserRoleDao;
import com.debug.pmp.server.service.SysUserRoleService;
import com.google.common.base.Joiner;
import net.sf.jsqlparser.statement.select.Join;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * @author Administrator
 */
@Service("sysUserRoleService")
public class SysUserRoleServiceImpl extends ServiceImpl<SysUserRoleDao, SysUserRoleEntity> implements SysUserRoleService {

    /**
     * 删除
     * @param roleIds 角色ids
     */
    @Override
    public void deleteBatch(List<Long> roleIds) {
        String delIds = Joiner.on(",").join(roleIds);
        baseMapper.deleteBatch(CommonUtil.concatStrToChar(delIds,","));
    }

    /**
     * 添加用户，关联的角色
     * @param userId
     * @param roleIdList
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(Long userId, List<Long> roleIdList) {

        this.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",userId));

        SysUserRoleEntity entity;
        for (Long roleId : roleIdList) {
            entity = new SysUserRoleEntity();
            entity.setUserId(userId);
            entity.setRoleId(roleId);
            this.save(entity);
        }

    }

    /**
     * 根据用户ID查角色id
     * @param userId
     * @return
     */
    @Override
    public List<Long> queryRoldIdList(Long userId) {
        return baseMapper.queryRoleIdList(userId);
    }
}
