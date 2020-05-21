package com.debug.pmp.api.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 调用者获取授权的accessToken时传递的参数
 * @Author:debug (SteadyJack)
 * @Link: weixin-> debug0868 qq-> 1948831260
 * @Date: 2019/12/5 17:50
 **/
@Data
public class ClientDto implements Serializable{

    /*private String clientId;
    private String clientSecret;*/

    //TODO:将由clientId、clientSecret、sysCode加密生成
    //TODO:(1)这三个参数将由 中台 私底下告诉-发给 需要调用服务的调用方
    //TODO:(2)加密生成：AES-PKCS5填充模式生成，密钥也是由 中台 私底下告诉-发给 需要调用服务的调用方
    private String encryptStr;

    //随机字符串，可以随意生成
    private String nonceStr;
}


















