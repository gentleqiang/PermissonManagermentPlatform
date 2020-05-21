package com.debug.pmp.server.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.debug.pmp.model.entity.SysConfig;
import com.debug.pmp.model.entity.SysMenuEntity;

import java.util.List;

/**
 * @author gentleman_qiag
 */
public interface SysConfigService extends IService<SysConfig>{
     List<SysConfig> getAll() throws Exception;
}
