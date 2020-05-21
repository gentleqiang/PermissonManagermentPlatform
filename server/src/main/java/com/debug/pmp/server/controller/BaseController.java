package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.server.init.SystemInitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author gentleman_qiang
 */
@Controller
@RequestMapping("/base")
public class BaseController {

    private static final Logger log = LoggerFactory.getLogger(BaseController.class);

    @Autowired
    private SystemInitService systemInitService;
    /**
     * 第一个案例
     *
     * @param name
     * @return
     */
    @RequestMapping(value = "/info", method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse info(String name) {
        BaseResponse response = new BaseResponse(StatusCode.Success);

        if (null == name) {
            name = "权限管理平台";
        }
        response.setData(name);

        return response;
    }

    /**
     * 第二个案例
     *
     * @return
     */
    @RequestMapping(value = "/page", method = RequestMethod.GET)
    public String page(String name, ModelMap modelMap) {
        if (null == name) {
            name = "企业权限管理平台";
        }
        modelMap.put("name", name);
        modelMap.put("app", "WhatFuck");
        return "pageOne";
    }

    /*
    * 产品经理要 "新增","删减"个别系统编码，如何在不重启项目的前提下“无缝，平滑，实时”
    * 实现业务系统编码的自动更新呢
    * */
    //上不了台面的小技巧
    @RequestMapping(value = "/flush/redis/config",method = RequestMethod.GET)
    @ResponseBody
    public BaseResponse flushConfig(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            systemInitService.init();
        }catch (Exception e){
            response = new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

}











