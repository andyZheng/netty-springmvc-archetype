/*
 * 文件名：CommonService.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.services;

import java.io.Serializable;
import java.util.List;

import com.platform.common.paging.Page;

/**
 * 功能描述：<code>CommonService</code>类针对业务服务层抽象公共服务接口。<p>
 * 本类是业务服务层相关接口的高度抽象，只提供CRUD基本接口，具体业务接口需要由相关子服务接口定义。
 *
 * @author   Andy.zheng andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月2日 下午4:10:17
 * @since    Common-Platform/Common Service API 1.0
 */
public interface CommonService<E, PK extends Serializable> {
    
    /**
     * 功能描述：查询所有记录。
     *
     * @return 查询到的所有记录。
     */
    public List<E> queryAll();
    
    /**
     * 功能描述：查询所有记录。<p>
     * 支持自定义排序功能。
     *
     * @param sortBy    排序方式。
     * @return          查询到的所有记录。
     */
    public List<E> queryAll(final String sortBy);
    
    
    /**
     * 功能描述：分页查询。<p>
     * 封装分页相关参数进行分页查询。
     *
     * @param page      当前分页对象。
     * @param sortBy    排序方式。
     * @return          当前分页查询结果。
     */
    public Page queryByPaging(final Page page, final String... sortBy);
    
    
    
    /**
     * 功能描述：根据当前实体对象唯一标识值进行查询。<p>
     * 默认根据主键ID进行查询。
     *
     * @param id    当前实体主键。
     * @return      查询到的实体对象。
     */
    public E getById(final PK id);
    
    /**
     * 功能描述：保存实体对象。
     *
     * @param entity    待保存的实体对象。
     * @return          保存结果信息。
     */
    public E save(final E entity);
    
    
    /**
     * 功能描述：更新实体对象。
     *
     * @param entity    待更新的实体对象。
     * @return          更新结果信息。
     */
    public Boolean update(final E entity);
    
    
    /**
     * 功能描述：删除某个实体对象。
     *
     * @param entity    待删除的实体对象。
     * @return          删除结果信息。
     */
    public Boolean remove(final E entity);
}