package com.debug.pmp.api.response;

import java.io.Serializable;

/**
 * dubbo服务调用之间的响应模型
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/3 10:57
 **/
public class CenterResponse <T> implements Serializable{

    private Integer code;
    private String msg;

    private T data;

    public CenterResponse(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public CenterResponse() {
    }

    public CenterResponse(CenterStatus centerStatus) {
        this.code = centerStatus.getCode();
        this.msg = centerStatus.getMsg();
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}