/*
 * 文件名：CommonServiceImpl.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.services.impl;

import java.io.Serializable;
import java.util.List;

import com.platform.common.dao.CommonDao;
import com.platform.common.paging.Page;
import com.platform.common.services.CommonService;

/**
 * 功能描述：<code>BaseCommonService</code>类主要实现了公共服务接口{@link CommonService}定义的相关业务服务接口。
 * <p>
 * Note:本服务实现类基于高层接口实现，因此将它定义为抽象类，各系统业务服务子类需要继承本类并需要根据业务的需要定义各自的个性化服务接口。
 *
 * @author   andy.zheng andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月3日 上午11:33:22
 * @since    Common-Platform/Common Service API 1.0
 */
public abstract class BaseCommonService<E, PK extends Serializable> implements CommonService<E, PK> {

    /**
     * 功能描述：获取公共数据访问对象。
     *
     * @return  公共数据访问对象。
     */
    public abstract CommonDao<E, PK> getCommonDao();
    
    /* 
     * @see com.platform.common.services.CommonService#queryAll()
     */
    public List<E> queryAll() {
        return this.getCommonDao().queryAll();
    }

    /* 
     * @see com.platform.common.services.CommonService#queryAll(java.lang.String)
     */
    public List<E> queryAll(String sortBy) {
        return this.getCommonDao().queryAll(sortBy);
    }

    /* 
     * @see com.platform.common.services.CommonService#queryByPaging(com.platform.common.paging.Page, java.lang.String[])
     */
    public Page queryByPaging(Page page, String... sortBy) {
        // 统计分页记录
        Long totalCount = this.getCommonDao().countByConditions(page.getFilters());
        page.setTotalCount(totalCount);
        
        // 分页查询
        List<E> resultList = this.getCommonDao().queryByConditions(page.getOffset().intValue(), page.getPageSize(), page.getFilters(), sortBy);
        page.setResultList(resultList);
        
        return page;
    }

    /* 
     * @see com.platform.common.services.CommonService#getById(java.io.Serializable)
     */
    public E getById(PK id) {
        return this.getCommonDao().getById(id);
    }

    /* 
     * @see com.platform.common.services.CommonService#save(java.lang.Object)
     */
    public E save(E entity) {
        return this.getCommonDao().save(entity);
    }

    /* 
     * @see com.platform.common.services.CommonService#update(java.lang.Object)
     */
    public Boolean update(E entity) {
        return this.getCommonDao().update(entity);
    }

    /* 
     * @see com.platform.common.services.CommonService#remove(java.lang.Object)
     */
    public Boolean remove(E entity) {
        return this.getCommonDao().remove(entity);
    }

}