package com.debug.pmp.server.service.common.impl;

import com.debug.pmp.api.dto.LoginDto;
import com.debug.pmp.api.dto.UpdatePsdDto;
import com.debug.pmp.api.dto.UserDto;
import com.debug.pmp.api.response.CenterStatus;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenuEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.SysUserDao;
import com.debug.pmp.server.service.SysMenuService;
import com.debug.pmp.server.service.SysUserPostService;
import com.debug.pmp.server.service.SysUserService;
import com.debug.pmp.server.service.common.IUservice;
import com.debug.pmp.server.shiro.ShiroUtil;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.DisabledAccountException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gentleman_qiang
 */
@Service
public class UserService implements IUservice {

    protected static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysMenuService sysMenuService;

    @Autowired
    private SysUserService sysUserService;


    @Override
    public UserDto login(LoginDto loginDto) throws Exception {
        SysUserEntity userEntity = sysUserDao.selectByUserName(loginDto.getUserName());
        if(userEntity == null){
           throw new UnknownAccountException(CenterStatus.UserAccountNotExist.getMsg());
        }
        if(userEntity.getStatus() == 0){
            throw new DisabledAccountException(CenterStatus.UserAccountHasBeenDisabled.getMsg());
        }

        String dtpPwd = ShiroUtil.sha256(loginDto.getPassword(), userEntity.getSalt());
        if(!dtpPwd.equals(userEntity.getPassword())){
            throw new IncorrectCredentialsException(CenterStatus.OldPasswordNotMatch.getMsg());
        }
        log.info("--进入到登录模块--");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity,userDto);
        userDto.setPassword(null);
        userDto.setSalt(null);

        String postName = sysUserPostService.getPostNamesByUserId(userEntity.getUserId());
        userDto.setPostName(postName);
        return userDto;

    }

    //TODO 根据用户姓名获取授权列表
    @Override
    public void getPermsByUserName(String userName, String sysCode) {
        SysUserEntity userEntity = sysUserDao.selectByUserName(userName);
        if(userEntity.getUserId().equals(Constant.SUPER_ADMIN)){

        }

    }

    //TODO 根据用户id获取授权列表
    @Override
    public void getPermsByUserId(Long userId, String sysCode) {

    }

    //TODO 修改用户密码
    @Override
    public Boolean updatePassword(UpdatePsdDto updatePsdDto) {
        SysUserEntity userEntity = sysUserDao.selectByUserName(updatePsdDto.getUserName());
        if(userEntity == null){
            throw new UnknownAccountException(CenterStatus.UserAccountNotExist.getMsg());
        }
        if(userEntity.getStatus() == 0){
            throw new DisabledAccountException(CenterStatus.UserAccountHasBeenDisabled.getMsg());
        }
        final  String salt = userEntity.getSalt();
        String oldPwd = ShiroUtil.sha256(updatePsdDto.getOldPsd(),salt);
        if(!oldPwd.equals(userEntity.getPassword())){
            throw new IncorrectCredentialsException(CenterStatus.OldPasswordNotMatch.getMsg());
        }
        log.info("--进入到修改模块--");

        return sysUserService.updataPassword(userEntity.getUserId(),oldPwd,updatePsdDto.getNewPsd());
    }


    private void getPerms(final Long userId,final String sysCode)throws Exception{
        //TODO 获取用户操作权限列表
        Set<String> perms = Sets.newHashSet();
        if(Objects.equals(userId,Constant.SUPER_ADMIN)){
            List<SysMenuEntity> menuList = sysMenuService.list();
            if(menuList.size() >0 && !menuList.isEmpty()){
                 perms = menuList.stream().map(SysMenuEntity::getPerms).collect(Collectors.toSet());
            }
        }else {
            perms = sysUserDao.queryAllPermsV2(userId, sysCode);
        }
        //TODO 对没一个授权码进行 ，的分割
        Set<String> userPerms = Sets.newHashSet();
        if(perms!=null && !perms.isEmpty()){
            for (String perm : perms) {
                if(StringUtils.isNotBlank(perm)){
                    String[] split = StringUtils.split(perm.trim(), ",");
                    userPerms.addAll(Arrays.asList(split));
                }
            }
        }

        //TODO 获取当前用户的菜单资源类表
    }



}
