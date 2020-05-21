package com.debug.pmp.server.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.debug.pmp.common.response.StatusCode;
import com.debug.pmp.common.utils.PageUtil;
import com.debug.pmp.common.utils.QueryUtil;
import com.debug.pmp.model.entity.SysPostEntity;
import com.debug.pmp.model.mapper.SysPostDao;
import com.debug.pmp.server.service.SysPostService;
import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Map;

/**
 * @author gentleman_qinag
 */
@Service("sysPostService")
public class SysPostServiceImpl extends  ServiceImpl<SysPostDao, SysPostEntity> implements SysPostService {

    private static final Logger log = LoggerFactory.getLogger(SysPostServiceImpl.class);
    @Override
    public PageUtil queryPage(Map<String, Object> params) {

        String search =  params.get("search") == null ? "" :params.get("search").toString();

        IPage<SysPostEntity> queryPage = new QueryUtil<SysPostEntity>().getQueryPage(params);

        QueryWrapper<SysPostEntity> wrapper = new QueryWrapper<SysPostEntity>()
                .like(StringUtils.isNotEmpty(search), "post_code", search)
                .or(StringUtils.isNotEmpty(search))
                .like(StringUtils.isNotEmpty(search), "post_name", search);

        IPage<SysPostEntity> page = this.page(queryPage, wrapper);

        return new PageUtil(page);
    }

    /**
     * 新增岗位
     * @param sysPostEntity
     */
    @Override
    public void savePost(SysPostEntity sysPostEntity) {

        if(this.getOne(new QueryWrapper<SysPostEntity>().eq
                ("post_code", sysPostEntity.getPostCode()))!=null){
            throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
        }
        sysPostEntity.setCreateTime(DateTime.now().toDate());
        save(sysPostEntity);
    }

    /**
     * 修改岗位
     * @param sysPostEntity
     */
    @Override
    public void updatePost(SysPostEntity sysPostEntity) {

        SysPostEntity byId = this.getById(sysPostEntity.getPostId());

        if(byId != null && !byId.getPostCode().equals(sysPostEntity.getPostCode())){
            if(this.getOne(new QueryWrapper<SysPostEntity>().eq
                    ("post_code", sysPostEntity.getPostCode()))!=null){
                throw new RuntimeException(StatusCode.PostCodeHasExist.getMsg());
            }
        }
        sysPostEntity.setCreateTime(DateTime.now().toDate());
        updateById(sysPostEntity);
    }

    /**
     * 批量删除岗位
     * @param ids
     */
    @Override
    public void deletePatch(Long[] ids) {
       this.removeByIds(Arrays.asList(ids));
    }

}
