package com.debug.pmp.model.entity;

import lombok.Data;

import java.io.Serializable;

@Data
public class ClientConfig implements Serializable{
    private Integer id;

    private String clientId;

    private String clientSecret;

    private String sysCode;

    private Integer isActive;
}