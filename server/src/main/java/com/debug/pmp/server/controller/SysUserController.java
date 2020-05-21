package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.SysRoleEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.server.service.SysDeptService;
import com.debug.pmp.server.service.SysPostService;
import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.shiro.ShiroUtil;
import com.google.common.collect.Maps;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Map;


/**
 * @author gentleman_qiang
 */
@RestController
@RequestMapping("sys/user")
public class SysUserController extends AbstractController{


    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysDeptService sysDeptService;

    /**
     * 回显用户信息
     * @return
     */
    @RequestMapping(value = "/info",method = RequestMethod.GET)
    public BaseResponse getUserInfo(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resultMap = Maps.newHashMap();
        try{
            resultMap.put("user",getUser());
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(resultMap);
        log.info("返回的信息为：{}",response);
        return response;
    }

    /*
    * 修改登录密码
    * */
    @RequestMapping(value = "/password",method = RequestMethod.POST)
    public BaseResponse upPass(String password ,String newPassword){
        if(StringUtils.isEmpty(password) && StringUtils.isEmpty(newPassword)){
            return new BaseResponse(StatusCode.PasswordCanNotBlank);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            SysUserEntity sysUserEntity = getUser();

            String oldPsd = ShiroUtil.sha256(password, sysUserEntity.getSalt());

            if(!oldPsd.equals(getUser().getPassword())){
                return new BaseResponse(StatusCode.OldPasswordNotMatch);
            }

            String newPsd = ShiroUtil.sha256(newPassword, sysUserEntity.getSalt());

            log.info("旧密码验证成功，开始更新新密码");

            sysUserService.updataPassword(getUser().getUserId(),oldPsd,newPsd);

        }catch (Exception e){
            return new BaseResponse(StatusCode.UpdatePasswordFail);
        }
        return response;
    }

    /**
     * 分页模糊
     * @param params
     * @return
     */
    @RequestMapping("/list")
    @RequiresPermissions({"sys:user:list"})
    public BaseResponse list(@RequestParam Map<String,Object> params){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            PageUtil pageUtil = sysUserService.queryPage(params);
            rstMap.put("page",pageUtil);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 新增用户
     * BindingResult  绑定结果
     * consumes       消耗
     * MediaType      媒体类型
     * @param sysUserEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"sys:user:save"})
    public BaseResponse<Object> sava(@RequestBody @Validated SysUserEntity sysUserEntity, BindingResult result){
        String checkResult = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse<>(StatusCode.Success);
        try {
            sysUserService.saveUser(sysUserEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 回显用户信息
     * @return
     */
    @RequestMapping(value = "/info/{userId}",method = RequestMethod.GET)
    @RequiresPermissions({"sys:user:info"})
    public BaseResponse info(@PathVariable Long userId){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> resultMap = Maps.newHashMap();
        try{
            SysUserEntity sysUserEntity = sysUserService.selectByUserId(userId);
            resultMap.put("user",sysUserEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(resultMap);
        return response;
    }

    /**
     * 修改用户信息
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"sys:user:update"})
    public BaseResponse update(@RequestBody @Validated SysUserEntity sysUserEntity, BindingResult result){
        String checkResult = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            sysUserService.updateUser(sysUserEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    /**
     * 删除用户信息
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @RequiresPermissions({"sys:user:delete"})
    public BaseResponse delete(@RequestBody Long[] userId){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            if(Arrays.asList(userId).contains(getUser().getUserId())){
                return new BaseResponse(StatusCode.CurrUserCanNotBeDelete);
            }
            if(Arrays.asList(userId).contains(Constant.SUPER_ADMIN)){
                return new BaseResponse(StatusCode.SysUserCanNotBeDelete);
            }
            sysUserService.deleteByUserId(userId);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    /**
     * 重置用户密码
     * @return
     */
    @RequestMapping(value = "/psd/reset")
    public BaseResponse reset(@RequestBody Long[] ids){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        if(ArrayUtils.contains(ids,Constant.SUPER_ADMIN) || ArrayUtils.contains(ids,getUser().getUserId())){
            return new BaseResponse(StatusCode.SysUserAndCurrUserCanNotResetPsd);
        }
        try{
            sysUserService.reset(ids);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }




}
