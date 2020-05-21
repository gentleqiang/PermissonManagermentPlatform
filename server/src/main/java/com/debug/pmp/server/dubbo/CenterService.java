package com.debug.pmp.server.dubbo;

import com.alibaba.dubbo.config.annotation.Service;
import com.debug.pmp.api.dto.LoginDto;
import com.debug.pmp.api.dto.UpdatePsdDto;
import com.debug.pmp.api.dto.UserDto;
import com.debug.pmp.api.response.CenterResponse;
import com.debug.pmp.api.response.CenterStatus;
import com.debug.pmp.api.service.ICenterService;
import com.debug.pmp.model.mapper.SysConfigMapper;
import com.debug.pmp.server.service.common.IUservice;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestParam;

import javax.ws.rs.*;
import java.awt.*;

/**
 * 提供服务的实现类
 * @author Administrator
 */
@Service(protocol = {"dubbo","rest"},timeout = 30000,version = "1.0",validation = "true")
@Path("/dubbo/center")
public class CenterService implements ICenterService {

    private static Logger log = LoggerFactory.getLogger(CenterService.class);

    @Autowired
    private IUservice iUservice;

    @Autowired
    private SysConfigMapper sysConfigMapper;

    @Override
    @Path("/login")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CenterResponse<UserDto> login(LoginDto loginDto) {
        CenterResponse response = new CenterResponse(CenterStatus.Success);
        if(StringUtils.isBlank(loginDto.getUserName()) || StringUtils.isBlank(loginDto.getPassword())){
            response = new  CenterResponse(CenterStatus.InvalidParams);
        }
        log.info("应用中台-生产者提供的dubbo接口服务-用户登录服务");
        try {
            UserDto userDto = iUservice.login(loginDto);
            response.setData(userDto);
        }catch (Exception e){
            log.error("应用中台-生产者提供的dubbo接口服务-用户登录-发生异常：{}",loginDto,e.getStackTrace());
            response = new  CenterResponse(CenterStatus.LoginFail.getCode(),e.getMessage());
        }
        return response;
    }

    @Override
    @Path("/perms/username")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CenterResponse getUserPermsByUserName(@QueryParam("userName") String userName, @QueryParam("sysCode") String sysCode) {
        CenterResponse response = new CenterResponse(CenterStatus.Success);
        if(StringUtils.isBlank(userName) || StringUtils.isBlank(sysCode)){
            response = new CenterResponse(CenterStatus.InvalidParams);
        }
        if(sysConfigMapper.selectByCode(sysCode)== null){
            response = new CenterResponse(CenterStatus.SysConfigCodeNotExist);
        }
        log.info("应用中台-生产者提供的dubbo接口服务-获取用户授权码模块");
        try {

        }catch (Exception e){
            log.error("应用中台-生产者提供的dubbo接口服务-获取用户授权码模块-发生异常：{}",  e.getStackTrace());
            response = new  CenterResponse(CenterStatus.LoginFail.getCode(),e.getMessage());
        }
        return response;
    }

    @Override
    @Path("/perms/userId")
    @GET
    @Produces(value = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CenterResponse getUserPermsByUserId(@QueryParam("userId") Long userId,@QueryParam("sysCode") String sysCode) {
        CenterResponse response = new CenterResponse(CenterStatus.Success);
        if(userId == null || StringUtils.isBlank(sysCode)){
            response = new  CenterResponse(CenterStatus.InvalidParams);
        }
        log.info("应用中台-生产者提供的dubbo接口服务-获取用户授权码模块");
        try {

        }catch (Exception e){
            log.error("应用中台-生产者提供的dubbo接口服务-获取用户授权码模块-发生异常：{}",  e.getStackTrace());
            response = new  CenterResponse(CenterStatus.LoginFail.getCode(),e.getMessage());
        }
        return response;
    }

    @Override
    @Path("/update/psd")
    @POST
    @Consumes(value = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @Produces(value = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public CenterResponse updateUserPassword(UpdatePsdDto updatePsdDto) {
        CenterResponse response = new CenterResponse(CenterStatus.Success);
        if(StringUtils.isBlank(updatePsdDto.getUserName()) || StringUtils.isBlank(updatePsdDto.getOldPsd())
        || StringUtils.isBlank(updatePsdDto.getNewPsd())){
            response = new CenterResponse(CenterStatus.InvalidParams);
        }
        log.info("应用中台-生产者提供的dubbo接口服务-修改用户密码");
        try {
           iUservice.updatePassword(updatePsdDto);
        }catch (Exception e){
            log.error("应用中台-生产者提供的dubbo接口服务-修改用户面-发生异常：{}",  e.getStackTrace());
            response = new  CenterResponse(CenterStatus.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
