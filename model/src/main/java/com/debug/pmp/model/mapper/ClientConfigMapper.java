package com.debug.pmp.model.mapper;

import com.debug.pmp.model.entity.ClientConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface ClientConfigMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ClientConfig record);

    int insertSelective(ClientConfig record);

    ClientConfig selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ClientConfig record);

    int updateByPrimaryKey(ClientConfig record);

    ClientConfig selectByIdSecret(@Param("clientId") String clientId, @Param("clientSecret") String clientSecret);

}