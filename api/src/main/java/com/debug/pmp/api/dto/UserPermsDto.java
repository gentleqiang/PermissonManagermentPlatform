package com.debug.pmp.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * 返回给到调用者（消费者）的  “当前用户的菜单资源和操作权限列表”
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/3 15:48
 **/
@Data
public class UserPermsDto implements Serializable{

    List<MenuDto> userMenus;

    Set<String> perms;

}