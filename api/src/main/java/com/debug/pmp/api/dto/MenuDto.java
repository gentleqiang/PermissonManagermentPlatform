package com.debug.pmp.api.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 面向dubbo服务调用者-菜单资源实体信息
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/3 15:34
 **/
@Data
public class MenuDto implements Serializable{

    private Long menuId;

    private Long parentId;

    private String parentName;

    private String name;

    private String url;

    private String perms;

    //类型= 0：目录   1：菜单   2：按钮
    private Integer type;

    //菜单图标
    private String icon;

    //排序
    private Integer orderNum;

    //所属系统编码
    private String sysCode;

    private String sysName;

    private Boolean open;

    private List<?> list;

}