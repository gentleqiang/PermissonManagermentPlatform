package com.debug.pmp.server.controller;

import org.apache.shiro.SecurityUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author gentleman_qiang
 */
@Controller
public class SysPageController {

    @RequestMapping("modules/{model}/{page}.html")
    public String page(@PathVariable String model,@PathVariable String page){
        return "modules/"+model+"/"+page;
    }

    @RequestMapping("login.html")
    public String login(){
        //优化用户体检，当用户登录以后，再打开页面，(isAuthenticated: 已认证)
        if(SecurityUtils.getSubject().isAuthenticated()){
             return "redirect:index.html";
        }
        return "login";
    }

    @RequestMapping("index.html")
    public String index(){
        return "index";
    }

    @RequestMapping("main.html")
    public String main(){
        return "main";
    }

    @RequestMapping("404.html")
    public String error(){
        return "404";
    }

}
