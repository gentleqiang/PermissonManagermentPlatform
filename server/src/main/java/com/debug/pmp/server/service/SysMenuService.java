package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysMenuEntity;

import java.util.List;

/**
 * @author gentleman_qiag
 */
public interface SysMenuService extends IService<SysMenuEntity> {

    List<SysMenuEntity> queryAll(String sysCode);

    List<SysMenuEntity> queryNotButtonList(String sysCode);

    List<SysMenuEntity> queryByParentId(Long menuId);

    void delete(Long menuId);

    List<SysMenuEntity> getUserMenuList(Long currUserId);

    SysMenuEntity getInfo(Long menuId);
}
