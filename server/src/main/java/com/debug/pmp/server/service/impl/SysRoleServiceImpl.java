package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysRoleEntity;
import com.debug.pmp.model.entity.SysRoleMenuEntity;
import com.debug.pmp.model.mapper.SysRoleDao;
import com.debug.pmp.server.service.SysRoleDeptService;
import com.debug.pmp.server.service.SysRoleMenuService;
import com.debug.pmp.server.service.SysRoleService;
import com.debug.pmp.server.service.SysUserRoleService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author gentleman_qiang
 */
@Service("SysRoleService")
public class SysRoleServiceImpl extends ServiceImpl<SysRoleDao, SysRoleEntity> implements SysRoleService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;

    @Autowired
    private SysRoleDeptService sysRoleDeptService;

    @Autowired
    private SysUserRoleService sysUserRoleService;


    @Override
    public PageUtil querypage(Map<String, Object> params) {
        String search = params.get("search")!= null ? String.valueOf(params.get("search")):"";

        IPage<SysRoleEntity> queryPage = new QueryUtil<SysRoleEntity>().getQueryPage(params);
        QueryWrapper<SysRoleEntity> queryWrapper = new QueryWrapper<SysRoleEntity>().like(StringUtils.isNotEmpty(search),"role_name", search);
        IPage<SysRoleEntity> page = this.page(queryPage, queryWrapper);
        return new PageUtil(page);
    }

    /**
     * 插入角色
     * @param sysRoleEntity   角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveRole(SysRoleEntity sysRoleEntity) {
        sysRoleEntity.setCreateTime(DateTime.now().toDate());
        this.save(sysRoleEntity);

        //插入角色 维护关联角色~菜单表
        sysRoleMenuService.saveOrUpdate(sysRoleEntity.getRoleId(),sysRoleEntity.getMenuIdList());

        //插入角色 维护关联角色~部门表
        sysRoleDeptService.saveOrUpdate(sysRoleEntity.getRoleId(),sysRoleEntity.getDeptIdList());
    }

    /**
     * 获取详情
     * @param roleId
     * @return
     */
    @Override
    public SysRoleEntity queryByRoleId(Long roleId) {


        SysRoleEntity byId = this.getById(roleId);
        //查关联角色-菜单表
        List<Long> menuIdList = sysRoleMenuService.queryByRoleId(roleId);

        //查关联角色-部门表
        List<Long> deptIdList = sysRoleDeptService.queryByRoleId(roleId);

        byId.setMenuIdList(menuIdList);

        byId.setDeptIdList(deptIdList);

        return byId;
    }

    /**
     * 修改菜单模块
     * @param sysRoleEntity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateRole(SysRoleEntity sysRoleEntity) {
        this.updateById(sysRoleEntity);

        //插入角色 维护关联角色~菜单表
        sysRoleMenuService.saveOrUpdate(sysRoleEntity.getRoleId(),sysRoleEntity.getMenuIdList());

        //插入角色 维护关联角色~部门表
        sysRoleDeptService.saveOrUpdate(sysRoleEntity.getRoleId(),sysRoleEntity.getDeptIdList());

    }

    /**
     * 删除角色
     * @param roleId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteBatch(Long[] roleId) {
        List<Long> roleIds = Arrays.asList(roleId);
        //删除角色表本身的
        removeByIds(roleIds);
        //删除角色-菜单表
        sysRoleMenuService.deleteBatch(roleIds);
        //删除角色-部门表
        sysRoleDeptService.deleteBatch(roleIds);
        //删除角色-用户表
        sysUserRoleService.deleteBatch(roleIds);
    }

    @Override
    public List<String> queryRolename() {
        return baseMapper.queryRoleName();
    }
}
