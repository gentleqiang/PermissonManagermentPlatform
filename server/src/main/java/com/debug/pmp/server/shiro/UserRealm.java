package com.debug.pmp.server.shiro;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.model.entity.SysMenuEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.mapper.SysUserDao;
import com.debug.pmp.server.service.SysMenuService;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.CredentialsMatcher;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author gentleman_qiang
 */
@Component
public class UserRealm extends AuthorizingRealm {

    private static final Logger log = LoggerFactory.getLogger(UserRealm.class);

    @Autowired
    private SysUserDao sysUserDao;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 资源-权限分配~授权 (当前用户的什么角色，拥有什么权限类表，塞进权限控制字段)
     * @param principalCollection 本金
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        /*//获取当前用户的实例
        SysUserEntity userEntity = (SysUserEntity) principalCollection.getPrimaryPrincipal();
        Long userId = userEntity.getUserId();
        List<String> permsList = Lists.newArrayList();
        if(userId.equals(Constant.SUPER_ADMIN)){
            List<SysMenuEntity> menuList = sysMenuService.list();
            if(!menuList.isEmpty() && menuList != null){
                permsList = menuList.stream().map(SysMenuEntity::getPerms).collect(Collectors.toList());
            }
        }else {
           permsList = sysUserDao.queryAllPerms(userId);
        }
        Set<String> stringPermissions = Sets.newHashSet();
        if(permsList != null && !permsList.isEmpty()){
            for (String perm : permsList) {
                stringPermissions.addAll(Arrays.asList(StringUtils.split(perm.trim(),",")));
            }
        }
        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();
        info.setStringPermissions(stringPermissions);
        **/
        return null;
    }

    /**
     * 用户认证-登录认证
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;

        final String username = token.getUsername();
        final String password = String.valueOf(token.getPassword());
        log.info("用户名:{},密码:{}",username,password);

        SysUserEntity entity = sysUserDao.selectOne(new QueryWrapper<SysUserEntity>().eq("username", username));
        log.info("用户的信息为：{}",entity.toString());
        if(null == entity){
           throw new UnknownAccountException("用户名不存在");
        }

        if(0 == entity.getStatus()){
           throw new DisabledAccountException("该用户名的账户被锁定");
        }

        //第一种，用明文适配
        /*if(!entity.getPassword().equals(password)){
            throw new IncorrectCredentialsException("密码不匹配，再试一次");
        }

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(entity, password,getName());*/
        //第二种（用shiro自带的密码适配器进行验证）

        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(entity, entity.getPassword(), ByteSource.Util.bytes(entity.getSalt()), getName());

        //第三种（用工具类，自定义加密适配）
        /*String realPassword = ShiroUtil.sha256(password, entity.getSalt());
        if(!realPassword.equals(entity.getPassword())){
            throw new IncorrectCredentialsException("密码不匹配，再试一次");
        }
        SimpleAuthenticationInfo info = new SimpleAuthenticationInfo(entity, password,getName());*/

        return info;
    }

    /**
     * 密码验证器~匹配逻辑 ~ 第二种验证逻辑
     * setCredentialsMatcher 设置凭据匹配器
     * HashedCredentialsMatcher 哈希凭证匹配器
     * @param credentialsMatcher
     */
    @Override
    public void setCredentialsMatcher(CredentialsMatcher credentialsMatcher) {
        HashedCredentialsMatcher shaCredentialsMatcher = new HashedCredentialsMatcher();
        //设置哈希算法名称
        shaCredentialsMatcher.setHashAlgorithmName(ShiroUtil.hashAlgorithmName);
        //设置哈希迭代
        shaCredentialsMatcher.setHashIterations(ShiroUtil.hashIterations);
        super.setCredentialsMatcher(shaCredentialsMatcher);
    }

    //自定义的UserRealm（用户领域）继承AuthorizingRealm（授权领域），也继承了两个方法分别是：
    //doGetAuthorizationInfo(获取授权信息)，doGetAuthenticationInfo（获取身份验证信息）
}
