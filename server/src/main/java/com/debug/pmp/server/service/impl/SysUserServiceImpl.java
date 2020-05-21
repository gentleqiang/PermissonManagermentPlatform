package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.response.BaseResponse;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.Constant;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysDeptEntity;
import com.debug.pmp.model.entity.SysUserEntity;
import com.debug.pmp.model.entity.SysUserPostEntity;
import com.debug.pmp.model.entity.SysUserRoleEntity;
import com.debug.pmp.model.mapper.SysUserDao;
import com.debug.pmp.server.service.*;
import com.debug.pmp.server.shiro.ShiroUtil;
import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;

/**
 * @author gentleman_qiang
 */
@Service("sysUserService")
public class SysUserServiceImpl extends ServiceImpl<SysUserDao, SysUserEntity> implements SysUserService {

    @Autowired
    private SysDeptService sysDeptService;

    @Autowired
    private SysUserPostService sysUserPostService;

    @Autowired
    private SysUserRoleService sysUserRoleService;
    /**
     *
     * @param userId         用户Id
     * @param oldPassword    旧密码
     * @param newPassword    新密码
     * @return
     */
    @Override
    public boolean updataPassword(Long userId, String oldPassword, String newPassword) {
        SysUserEntity userEntity = new SysUserEntity();
        userEntity.setPassword(newPassword);
        return this.update(userEntity,new QueryWrapper<SysUserEntity>().eq("user_Id",userId)
                .eq("password",oldPassword));
    }

    /**
     * 分页模糊查询
     * @param params
     * @return
     */
    @Override
    public PageUtil queryPage(Map<String, Object> params) {
        String search = (null != params.get("username"))?String.valueOf(params.get("username")):"";

        IPage<SysUserEntity> queryPage = new QueryUtil<SysUserEntity>().getQueryPage(params);

        QueryWrapper<SysUserEntity> queryWrapper = new QueryWrapper<SysUserEntity>()
                .like(StringUtils.isNotEmpty(search), "username", search)
                .or(StringUtils.isNotEmpty(search))
                .like(StringUtils.isNotEmpty(search), "name", search);

        IPage<SysUserEntity> page = this.page(queryPage, queryWrapper);

        SysDeptEntity sysDeptEntity;
        for (SysUserEntity userEntity : page.getRecords()) {
            try {
                sysDeptEntity = sysDeptService.getById(userEntity.getUserId());
                userEntity.setDeptName((sysDeptEntity != null && StringUtils.isNotBlank(sysDeptEntity.getName())?sysDeptEntity.getName():""));

                String postName = sysUserPostService.getPostNamesByUserId(userEntity.getUserId());
                userEntity.setPostName(postName);
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return new PageUtil(page);
    }

    /**
     * 新增用户
     * @param sysUserEntity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void saveUser(SysUserEntity sysUserEntity) {

        SysUserEntity username = this.getOne(new QueryWrapper<SysUserEntity>().eq("username", sysUserEntity.getUsername()));
        if(username != null){
            throw new RuntimeException("用户名已存在");
        }
        //添加用户
        sysUserEntity.setCreateTime(DateTime.now().toDate());
        //随机生成的盐
        String salt = RandomStringUtils.randomAlphanumeric(20);
        String password = ShiroUtil.sha256(sysUserEntity.getPassword(), salt);
        sysUserEntity.setSalt(salt);
        sysUserEntity.setPassword(password);
        this.save(sysUserEntity);

        //维护用户-角色表
        sysUserRoleService.saveRole(sysUserEntity.getUserId(),sysUserEntity.getRoleIdList());
        //维护用户-岗位表
        sysUserPostService.savePost(sysUserEntity.getUserId(),sysUserEntity.getPostIdList());




    }

    /**
     * 回显用户信息
     * @param userId
     * @return
     */
    @Override
    public SysUserEntity selectByUserId(Long userId) {
        SysUserEntity byId = this.getById(userId);

        //查用户-角色表
        List<Long> roldIdList = sysUserRoleService.queryRoldIdList(byId.getUserId());

        //查用户-岗位表
        List<Long> postIdList = sysUserPostService.queryPostIdList(byId.getUserId());

        byId.setRoleIdList(roldIdList);
        byId.setPostIdList(postIdList);

        return byId;
    }

    /**
     * 修改用户信息
     * @param sysUserEntity
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SysUserEntity sysUserEntity) {
        SysUserEntity byId = this.getById(sysUserEntity.getUserId());
        if(byId == null){
            return ;
        }

        if(!byId.getUsername().equals(sysUserEntity.getUsername())){
            SysUserEntity username = this.getOne(new QueryWrapper<SysUserEntity>().eq("username", sysUserEntity.getUsername()));
            if(username != null){
                throw new RuntimeException("修改后的用户名已存在");
            }

        }

        if(StringUtils.isNotBlank(sysUserEntity.getPassword())){
            String password = ShiroUtil.sha256(sysUserEntity.getPassword(), byId.getSalt());
            sysUserEntity.setPassword(password);
        }

        this.save(sysUserEntity);

        //维护用户-角色表
        sysUserRoleService.saveRole(sysUserEntity.getUserId(),sysUserEntity.getRoleIdList());
        //维护用户-岗位表
        sysUserPostService.savePost(sysUserEntity.getUserId(),sysUserEntity.getPostIdList());


    }

    /**
     * 删除用户信息的同时也删除岗位，角色信息
     * @param userId
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteByUserId(Long[] userId) {

        this.removeByIds(Arrays.asList(userId));
        for (Long uId : userId) {
            //清楚用户-关联的角色信息
            sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId));
           //清楚用户-关联的岗位信息
            sysUserPostService.remove(new QueryWrapper<SysUserPostEntity>().eq("user_id",uId));

        }

        //第二种
        //Arrays.asList(userId).stream().forEach(uId -> sysUserRoleService.remove(new QueryWrapper<SysUserRoleEntity>().eq("user_id",uId)));


    }

    /**
     * 批量重置密码
     * @param ids
     * @return
     */
    @Override
    public void reset(Long[] ids) {
        List<Long> idList = Arrays.asList(ids);
        SysUserEntity entity = new SysUserEntity();
        idList.stream().forEach(userId -> {
            String salt = RandomStringUtils.randomAlphanumeric(20);
            String newPsd = ShiroUtil.sha256(Constant.DefaultPassword, salt);
            entity.setSalt(salt);
            entity.setPassword(newPsd);
            update(entity,new QueryWrapper<SysUserEntity>().eq("user_id",userId));
        });
    }
}
