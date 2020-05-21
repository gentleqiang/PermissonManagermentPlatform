package com.debug.pmp.model.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class ClientToken implements Serializable{
    private Integer id;

    private String clientId;

    private String accessToken;

    private Byte isActive=1;

    private Date createTime;

}