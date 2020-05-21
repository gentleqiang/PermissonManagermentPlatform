package com.debug.pmp.server.controller;


import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.SysPostEntity;
import com.debug.pmp.server.service.SysPostService;
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

/**
 * 岗位模块
 * @author gentleman_qiang
 */
@RestController
@RequestMapping("/sys/post")
public class SysPostController extends AbstractController{

    @Autowired
    private SysPostService sysPostService;

    @RequestMapping("/list")
    public BaseResponse queryPage(@RequestParam Map<String,Object> paramMap){
        BaseResponse<Object> response = new BaseResponse<>(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try{
            PageUtil page = sysPostService.queryPage(paramMap);
            log.info("返回到的详情：{}",page);
            rstMap.put("page",page);
        }catch (Exception e){
            return new BaseResponse<>(StatusCode.Fail);
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 新增岗为
     * BindingResult  绑定结果
     * consumes       消耗
     * MediaType      媒体类型
     * @param sysPostEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse<Object> sava(@RequestBody @Validated SysPostEntity sysPostEntity, BindingResult result){


        String checkResult = ValidatorUtil.checkResult(result);

        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse<>(StatusCode.Success);
        try {
            log.info("接收到数据，开始新增岗位");
            sysPostService.savePost(sysPostEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获取详情
     * @param id
     * @return
     */
    @RequestMapping("/info/{id}")
    public BaseResponse info(@PathVariable Long id){
        if(null == id || id <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            SysPostEntity post = sysPostService.getById(id);
            rstMap.put("post",post);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 修改岗为
     * BindingResult  绑定结果
     * consumes       消耗
     * MediaType      媒体类型
     * @param sysPostEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse<Object> update(@RequestBody @Validated SysPostEntity sysPostEntity, BindingResult result){

        String checkResult = ValidatorUtil.checkResult(result);

        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }

        if(sysPostEntity.getPostId() == null || sysPostEntity.getPostId() <= 0){
            return new BaseResponse(StatusCode.InvalidCode);
        }
        BaseResponse response = new BaseResponse<>(StatusCode.Success);
        try {
            log.info("接收到数据，开始修改岗位");
            sysPostService.updatePost(sysPostEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 删除岗为
     *
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse<Object> delete(@RequestBody Long[] ids){
        BaseResponse response = new BaseResponse<>(StatusCode.Success);
        try {
            log.info("接收到数据，开始删除岗位:{}", Arrays.asList(ids));
            sysPostService.deletePatch(ids);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获得岗位名称
     * @param
     * @return
     */
    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            List<SysPostEntity> list = sysPostService.list();
            rstMap.put("list",list);
        }catch (Exception e){
            return  new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(rstMap);
        return response;
    }
}
