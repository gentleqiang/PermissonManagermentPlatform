package com.debug.pmp.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Administrator
 */
@Data
@TableName("sys_config")
public class SysConfig implements Serializable {
    private Integer id;

    private String code;

    private String name;

    private Byte isActive;

    private String remark;

    private Date createTime;

    private Date updateTime;

}