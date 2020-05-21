package com.debug.pmp.server.controller;

import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.ValidatorUtil;
import com.debug.pmp.model.entity.SysDeptEntity;
import com.debug.pmp.server.service.SysDeptService;
import com.google.common.collect.Maps;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * 部门模块
 * @author gentleman_qiang
 */
@RestController
@RequestMapping("/sys/dept")
public class SysDeptController extends AbstractController{

    @Autowired
    private SysDeptService sysDeptService;


    @RequestMapping("/list")
    public List<SysDeptEntity> list(){
      List<SysDeptEntity> sysDeptEntityList = sysDeptService.list();
        if(sysDeptEntityList != null || !sysDeptEntityList.isEmpty()){
            sysDeptEntityList.stream().forEach(new Consumer<SysDeptEntity>() {
                @Override
                public void accept(SysDeptEntity sysDeptEntity) {
                    SysDeptEntity byId = sysDeptService.getById(sysDeptEntity.getParentId());
                    sysDeptEntity.setParentName((byId != null && StringUtils.isNotBlank(byId.getName()))?byId.getName():"");
                }
            });
        }
        return sysDeptEntityList;

        /*return sysDeptService.queryAll(Maps.newHashMap());*/


    }

    @RequestMapping("/info")
    public BaseResponse info(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        Long deptId = 0L;
        try {
            //数据视野决定着的顶级部门id可能不是0
            /*if(getUser().getUserId() != Constant.SUPER_ADMIN){

            }*/
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        rstMap.put("deptId",deptId);
        response.setData(rstMap);
        return response;
    }

    @RequestMapping("/select")
    public BaseResponse select(){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        List<SysDeptEntity> deptList;
        try {
            deptList = sysDeptService.queryAll(Maps.newHashMap());
        }catch (Exception e){
             return new BaseResponse(StatusCode.Fail);
        }
        rstMap.put("deptList",deptList);
        response.setData(rstMap);
        return response;
    }

    /**
     * 新增部门
     * @param sysDeptEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse save(@RequestBody @Validated SysDeptEntity sysDeptEntity, BindingResult result){
        String checkResult = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidParams.getCode(),checkResult);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysDeptService.save(sysDeptEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 获取部门详情
     * @param deptId
     * @return
     */
    @RequestMapping("/detail/{deptId}")
    public BaseResponse detail(@PathVariable Long deptId){
       BaseResponse response = new BaseResponse(StatusCode.Success);
       Map<String,Object> rstMap = Maps.newHashMap();
       try {
           SysDeptEntity dept = sysDeptService.getById(deptId);
           rstMap.put("dept",dept);
       }catch (Exception e){
           return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
       }
       response.setData(rstMap);
       return response;
    }

    /**
     * 修改部门详情
     * @param sysDeptEntity
     * @param result
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse update(@RequestBody @Validated SysDeptEntity sysDeptEntity,BindingResult result){
        String checkResult = ValidatorUtil.checkResult(result);
        if(StringUtils.isNotBlank(checkResult)){
            return new BaseResponse(StatusCode.InvalidCode.getCode(),checkResult);
        }
        if(sysDeptEntity.getDeptId() ==null || sysDeptEntity.getDeptId()<=0){
            return new BaseResponse(StatusCode.InvalidCode);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            sysDeptService.updateById(sysDeptEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
         return response;
    }

    /**
     * 删除部门
     * @param deptId
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResponse delete(Long deptId){
        log.info("传过来的是deptId:{}",deptId);
        if(deptId == null || deptId <= 0){
            return new BaseResponse(StatusCode.InvalidCode);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try {
            //判断当前部门下是否有子部门，有的话提醒删完所有的子部门，再删除当前部门
            List<Long> deptIds = sysDeptService.queryDeptIds(deptId);
            if(deptIds != null || !deptIds.isEmpty()){
                  return new BaseResponse(StatusCode.DeptHasSubDeptCanNotBeDelete);
            }
            sysDeptService.removeById(deptId);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }
}
