package com.debug.pmp.server.service.common;

import com.debug.pmp.api.dto.LoginDto;
import com.debug.pmp.api.dto.UpdatePsdDto;
import com.debug.pmp.api.dto.UserDto;

/**
 * @author gentleman_qiang
 */
public interface IUservice {

    UserDto login(LoginDto loginDto)throws Exception;

    void getPermsByUserName(String userName,String sysCode);

    void getPermsByUserId(Long userId,String sysCode);

    Boolean updatePassword(UpdatePsdDto updatePsdDto);
}
