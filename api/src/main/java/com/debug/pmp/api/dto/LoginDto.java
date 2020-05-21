package com.debug.pmp.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * dubbo服务-登录需要提供的参数
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/3 10:59
 **/
@Data
public class LoginDto implements Serializable{

    private String userName;

    private String password;

}