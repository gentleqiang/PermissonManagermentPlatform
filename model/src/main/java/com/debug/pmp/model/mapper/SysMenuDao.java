package com.debug.pmp.model.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.debug.pmp.api.dto.MenuDto;
import com.debug.pmp.model.entity.SysMenuEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

//菜单管理
@Mapper
public interface SysMenuDao extends BaseMapper<SysMenuEntity> {

	//根据父级Id，查询子菜单
	List<SysMenuEntity> queryListParentIdCenterSys(Long parentId);

	List<SysMenuEntity> queryListParentId(Long parentId);

	List<MenuDto> queryListParentIdBySys(@Param("parentId") Long parentId, @Param("sysCode") String sysCode);



	//获取不包含按钮的菜单列表
	List<SysMenuEntity> queryNotButtonList(@Param("sysCode") String sysCode);

	//获取所有菜单
	List<SysMenuEntity> queryList(@Param("sysCode") String sysCode);

	SysMenuEntity selectById(@Param("menuId") Long menuId);
}
