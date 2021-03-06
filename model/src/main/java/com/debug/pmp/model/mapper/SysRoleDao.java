package com.debug.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debug.pmp.model.entity.SysRoleEntity;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

//角色管理
@Mapper
public interface SysRoleDao extends BaseMapper<SysRoleEntity> {

    List<String> queryRoleName();

}
