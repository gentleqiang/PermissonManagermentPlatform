package com.debug.pmp.server.controller;

import com.debug.pmp.model.entity.SysUserEntity;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;

/**
 * @author gentleman_qiang
 */
@Controller
public abstract class AbstractController {

    protected  Logger log = LoggerFactory.getLogger(getClass());

    //当用户登录成功时，会将用户信息登录到shiro当中，我们直接取就行
    protected SysUserEntity getUser(){
        return (SysUserEntity) SecurityUtils.getSubject().getPrincipal();
    }

}
