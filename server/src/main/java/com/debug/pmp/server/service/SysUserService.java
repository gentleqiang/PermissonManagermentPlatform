package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.model.entity.SysUserEntity;

import java.util.Map;

/**
 * @author gentleman_qiang
 */
public interface SysUserService extends IService<SysUserEntity> {

    boolean updataPassword(Long userId,String oldPassword,String newPassword);

    PageUtil queryPage(Map<String, Object> params);

    void saveUser(SysUserEntity sysUserEntity);

    SysUserEntity selectByUserId(Long userId);

    void updateUser(SysUserEntity sysUserEntity);

    void deleteByUserId(Long[] userId);

    void reset(Long[] ids);
}
