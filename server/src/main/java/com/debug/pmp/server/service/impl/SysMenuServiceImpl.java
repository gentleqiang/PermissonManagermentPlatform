package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.model.entity.SysMenuEntity;
import com.debug.pmp.model.entity.SysRoleMenuEntity;
import com.debug.pmp.model.mapper.SysMenuDao;
import com.debug.pmp.server.service.SysMenuService;
import com.debug.pmp.server.service.SysRoleMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author gentleman_qiang
 */
@Service("sysMenuService")
public class SysMenuServiceImpl extends ServiceImpl<SysMenuDao, SysMenuEntity> implements SysMenuService {

    @Autowired
    private SysRoleMenuService sysRoleMenuService;


    @Override
    public List<SysMenuEntity> queryAll(String sysCode) {
        return baseMapper.queryList(sysCode);
    }

    @Override
    public List<SysMenuEntity> queryNotButtonList(String sysCode) {
        return baseMapper.queryNotButtonList(sysCode);
    }

    @Override
    public List<SysMenuEntity> queryByParentId(Long parentId) {
        return baseMapper.queryListParentId(parentId);
    }

    @Override
    public void delete(Long menuId) {
        removeById(menuId);
        sysRoleMenuService.remove(new QueryWrapper<SysRoleMenuEntity>()
        .eq("menu_id",menuId));
    }

    @Override
    public List<SysMenuEntity> getUserMenuList(Long currUserId) {
        return null;
    }

    @Override
    public SysMenuEntity getInfo(Long menuId) {
        return null;
    }
}
