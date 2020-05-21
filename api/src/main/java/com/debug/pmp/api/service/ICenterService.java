package com.debug.pmp.api.service;


import com.debug.pmp.api.dto.LoginDto;
import com.debug.pmp.api.dto.UpdatePsdDto;
import com.debug.pmp.api.dto.UserDto;
import com.debug.pmp.api.response.CenterResponse;

/**
 * Created by Administrator on 2019/12/3.
 */
public interface ICenterService {

     CenterResponse login(LoginDto loginDto);

     CenterResponse getUserPermsByUserName(String userName,String sysCode);

     CenterResponse getUserPermsByUserId(Long userId,String sysCode);

     CenterResponse updateUserPassword(UpdatePsdDto updatePsdDto);

}
