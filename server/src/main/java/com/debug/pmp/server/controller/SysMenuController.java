package com.debug.pmp.server.controller;

import com.alibaba.druid.support.spring.stat.annotation.Stat;
import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenuEntity;
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
@RequestMapping("/sys/menu")
public class SysMenuController extends AbstractController{

    @Autowired
    private SysMenuService sysMenuService;

    @RequestMapping("/list")
    public List<SysMenuEntity> list(String sysCode){
        /*List<SysMenuEntity> list = sysMenuService.list();
        if(null != list || !list.isEmpty()){
            list.stream().forEach(new Consumer<SysMenuEntity>() {
                @Override
                public void accept(SysMenuEntity sysMenuEntity) {
                    if(sysMenuEntity.getType().equals(Constant.MenuType.BUTTON.getValue())){
                        SysMenuEntity byId = sysMenuService.getById(sysMenuEntity.getParentId());
                        sysMenuEntity.setParentName((byId !=null && StringUtils.isNotBlank(byId.getName())?byId.getName():""));
                    }
                }
            });
        }*/

        return sysMenuService.queryAll(sysCode);
    }

    /**
     * 获取菜单展示的树
     * @return
     */
    @RequestMapping("/select")
    public BaseResponse select(String sysCode){
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try {
            List<SysMenuEntity> list = sysMenuService.queryNotButtonList(sysCode);
            SysMenuEntity entity = new SysMenuEntity();
            entity.setMenuId(Constant.TOP_MENU_ID);
            entity.setName(Constant.TOP_MENU_NAME);
            entity.setParentId(-1L);
            entity.setOpen(true);
            list.add(entity);
            rstMap.put("menuList",list);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 新增
     * @return
     */
    @RequestMapping(value = "/save",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse save(@RequestBody SysMenuEntity sysMenuEntity){
        String checkReslt = this.validateForm(sysMenuEntity);
        if(StringUtils.isNotBlank(checkReslt)){
            return new BaseResponse(StatusCode.Fail.getCode(),checkReslt);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            sysMenuService.save(sysMenuEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail);
        }
        return response;
    }

    /**
     * 获取修改的目录详情
     * @return
     */
    @RequestMapping(value = "/info/{menuId}")
    public BaseResponse save(@PathVariable Long menuId){

        if(null == menuId){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        Map<String,Object> rstMap = Maps.newHashMap();
        try{
            SysMenuEntity byId = sysMenuService.getById(menuId);
            rstMap.put("menu",byId);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(rstMap);
        return response;
    }

    /**
     * 新增
     * @return
     */
    @RequestMapping(value = "/update",method = RequestMethod.POST,consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public BaseResponse update(@RequestBody SysMenuEntity sysMenuEntity){
        if(sysMenuEntity.getMenuId() == null){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        String checkReslt = this.validateForm(sysMenuEntity);
        if(StringUtils.isNotBlank(checkReslt)){
            return new BaseResponse(StatusCode.Fail.getCode(),checkReslt);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            sysMenuService.updateById(sysMenuEntity);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    /**
     * 删除
     * @return
     */
    @RequestMapping(value = "/delete",method = RequestMethod.POST)
    public BaseResponse delete(Long menuId){
        if(menuId == null || menuId <= 0){
            return new BaseResponse(StatusCode.InvalidParams);
        }
        BaseResponse response = new BaseResponse(StatusCode.Success);
        try{
            //判断当前状态下是否有子菜单，有则提示不能删除
            List<SysMenuEntity> list=sysMenuService.queryByParentId(menuId);
            if (list!=null && !list.isEmpty()){
                return new BaseResponse(StatusCode.MenuHasSubMenuListCanNotDelete);
            }
            sysMenuService.delete(menuId);
        }catch (Exception e){
            return new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        return response;
    }

    //验证参数是否正确
    private String validateForm(SysMenuEntity menu) {
        if (StringUtils.isBlank(menu.getName())) {
            return "菜单名称不能为空";
        }
        if (menu.getParentId() == null) {
            return "上级菜单不能为空";
        }

        //菜单
        if (menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (StringUtils.isBlank(menu.getUrl())) {
                return "菜单链接url不能为空";
            }
        }

        //上级菜单类型
        int parentType = Constant.MenuType.CATALOG.getValue();

        if (menu.getParentId() != 0) {
            SysMenuEntity parentMenu = sysMenuService.getById(menu.getParentId());
            parentType = parentMenu.getType();
        }

        //目录、菜单
        if (menu.getType() == Constant.MenuType.CATALOG.getValue() || menu.getType() == Constant.MenuType.MENU.getValue()) {
            if (parentType != Constant.MenuType.CATALOG.getValue()) {
                return "上级菜单只能为目录类型";
            }
            return "";
        }

        //按钮
        if (menu.getType() == Constant.MenuType.BUTTON.getValue()) {
            if (parentType != Constant.MenuType.MENU.getValue()) {
                return "上级菜单只能为菜单类型";
            }
            return "";
        }

        return "";
    }



}
