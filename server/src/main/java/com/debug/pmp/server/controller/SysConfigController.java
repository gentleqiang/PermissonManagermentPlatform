package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenuEntity;
import com.debug.pmp.server.service.SysConfigService;
import com.debug.pmp.server.service.SysMenuService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 菜单Controller
 * @author gentleman_qiang
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController extends AbstractController{

    @Autowired
    private SysConfigService sysConfigService;

    @RequestMapping("/list")
    public BaseResponse list(){
        BaseResponse response=new BaseResponse<>(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();
        try {
            resMap.put("list",sysConfigService.getAll());

        }catch (Exception e){
            response=new BaseResponse<>(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);

        return response;
    }

}
