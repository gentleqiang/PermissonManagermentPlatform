package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.ClientToken;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClientTokenMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientToken record);

    int insertSelective(ClientToken record);

    ClientToken selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientToken record);

    int updateByPrimaryKey(ClientToken record);

    ClientToken selectByToken(@Param("accessToken") String accessToken);
}