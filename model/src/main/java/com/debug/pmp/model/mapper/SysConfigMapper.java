package com.debug.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debug.pmp.model.entity.SysConfig;
import com.debug.pmp.model.entity.SysRoleDeptEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {
    int deleteByPrimaryKey(Integer id);

    int insert(SysConfig record);

    int insertSelective(SysConfig record);

    SysConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysConfig record);

    int updateByPrimaryKey(SysConfig record);

    SysConfig selectByCode(@Param("code") String code);

    List<SysConfig> selectAll();
}