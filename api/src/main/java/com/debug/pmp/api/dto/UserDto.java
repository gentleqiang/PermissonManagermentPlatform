package com.debug.pmp.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 用户信息响应实体
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/3 11:21
 **/
@Data
public class UserDto implements Serializable{

    private Long userId;

    private String username;

    private String name;

    private String password;

    private String salt;

    private String email;

    private String mobile;

    private Integer status;

    private Date createTime;

    private Long deptId;

    private String deptName;

    private String postName;

}