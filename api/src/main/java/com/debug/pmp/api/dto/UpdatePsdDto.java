package com.debug.pmp.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * dubbo服务-用户修改密码请求实体
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/3 16:22
 **/
@Data
public class UpdatePsdDto implements Serializable{

    private String userName;

    private String oldPsd;

    private String newPsd;

}