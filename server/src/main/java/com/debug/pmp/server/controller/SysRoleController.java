package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.SysRoleEntity;
import com.debug.pmp.server.service.SysRoleService;
import com.google.common.base.Joiner;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gentleman_qiang
 */
@RestController
@RequestMapping("/sys/role")
public class SysRoleController extends AbstractController {

    @Autowired
    private SysRoleService sysRoleService;

    @RequestMapping("/list")
    public BaseResponse list(@RequestParam Map<String,Object> paramMap){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            PageUtil pageUtil = sysRoleService.querypage(paramMap);
            rstMap.put("page",pageUtil);
        }catch (Exception e){
            return  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 新增角色
     * BindingResult  绑定结果
     * consumes       消耗
     * MediaType      媒体类型
     * @param sysRoleEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse<Object> sava(@RequestBody @Validated SysRoleEntity sysRoleEntity, BindingResult result){


        String checkResult = ValidatorUtil.checkResult(result);

        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse<>(StatusCode.Success);
        try {
            log.info("接收到数据，开始添加角色");
            sysRoleService.saveRole(sysRoleEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 查看详情
     * @param roleId
     * @return
     */
    @RequestMapping("/info/{roleId}")
    public BaseResponse info(@PathVariable Long roleId){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            SysRoleEntity sysRoleEntity = sysRoleService.queryByRoleId(roleId);
            rstMap.put("role",sysRoleEntity);
        }catch (Exception e){
            return  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 修改角色
     * BindingResult  绑定结果
     * consumes       消耗
     * MediaType      媒体类型
     * @param sysRoleEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse<Object> update(@RequestBody @Validated SysRoleEntity sysRoleEntity, BindingResult result){
        String checkResult = ValidatorUtil.checkResult(result);

        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse<>(StatusCode.Success);
        try {
            log.info("接收到数据，开始修改角色");
            sysRoleService.updateRole(sysRoleEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 删除
     * @param roleId
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse delete(@RequestBody Long[] roleId){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            log.info("接受数据：{}", Arrays.asList(roleId));
            sysRoleService.deleteBatch(roleId);
        }catch (Exception e){
            return  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获得角色名称
     * @param
     * @return
     */
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            List<SysRoleEntity> list = sysRoleService.list();
            rstMap.put("list",list);
        }catch (Exception e){
            return  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(rstMap);
        return response;
    }

}
