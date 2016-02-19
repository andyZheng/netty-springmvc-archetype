/*
 * 文件名：CommonDao.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台v1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：<code>CommonDao<code>是数据访问层高层接口定义类，用于抽象定义CRUD常规接口。<p>
 * 本类对系统开发用户提供的接口信息如下：
 * <ol>
 *      <li>形如<b>query*</b>命名的方法表示非主键相关查询接口；</li>
 *      <li>形如<b>get*</b>命名的方法表示根据主键查询接口；</li>
 *      <li>形如<b>count*</b>命名的方法表示统计接口；</li>
 *      <li>形如<b>isExist*</b>命名的方法表示是否存在判定接口；</li>
 *      <li>形如<b>save*</b>命名的方法表示保存接口；</li>
 *      <li>形如<b>update*</b>命名的方法表示统计接口；</li>
 *      <li>形如<b>remove*</b>命名的方法表示删除接口。</li>
 * </ol>
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月2日 下午4:11:19
 * @since    Common-Platform/DAO Component 1.0 
 */
public interface CommonDao<E, PK extends Serializable> {
    
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
     * 适用于无条件查询并支持自定义排序功能。
     *
     * @param offset    游标起始位置。   
     * @param pageSize  每页记录数。
     * @param sortBy    排序方式（可选参数）。
     * @return          当前分页所查询到的记录。
     */
    public List<E> queryAll(final Integer offset, final Integer pageSize, final String... sortBy);
    
    /**
     * 功能描述：根据当前实体的某个属性值查询。<p>
     * 适用于单条件查询，并支持自定义排序功能。
     *
     * @param property  待查询的属性名称。
     * @param value     待查询的属性值。
     * @param sortBy    排序方式（可选参数）。
     * @return          当前查询所匹配的记录。
     */
    public List<E> queryByProperty(final String property, final Object value, final String... sortBy);
    
    /**
     * 功能描述：根据当前实体的某个属性查询。<p>
     * 适用于单条件分页查询并支持自定义排序功能。
     *
     * @param offset        游标起始位置。   
     * @param pageSize      每页记录数。
     * @param property      待查询属性名称。
     * @param value         待查询的属性值。
     * @param sortBy        排序方式（可选参数）。
     * @return              当前查询所匹配的记录。
     */
    public List<E> queryByProperty(final Integer offset, final Integer pageSize, final String property, final Object value , final String... sortBy);
    
    /**
     * 功能描述：多条件查询。<p>
     * 以键值对结构封装多个查询参数进行查询，并支持自定义排序功能。
     *
     * @param conditions    待查询的条件。
     * @param sortBy        排序方式（可选参数）。
     * @return              当前查询条件所匹配的记录。
     */
    public List<E> queryByConditions(final Map<String, Object> conditions, final String... sortBy);
    
    
    /**
     * 功能描述：多条件分页查询。<p>
     * 以键值对结构封装多个查询参数进行分页查询，并支持自定义排序功能。
     *
     * @param offset        游标起始位置。   
     * @param pageSize      每页记录数。
     * @param conditions    待查询的条件。
     * @param sortBy        排序方式（可选参数）。
     * @return              当前查询条件所匹配的记录。
     */
    public List<E> queryByConditions(final Integer offset, final Integer pageSize, final Map<String, Object> conditions, final String... sortBy);
    
    
    
    /**
     * 功能描述：根据实体的某个属性进行查询。<p>
     * 适用于单条件查询返回为唯一记录的情况。
     *
     * @param property      待查询的属性名。
     * @param value         待查询的属性值。
     * @return              实体对象。
     */
    public E queryByUniqueProperty(final String property, final Object value);
    
    /**
     * 功能描述：以键值对的结构封装多个查询参数进行精确查询。<p>
     * 适用于多条件查询返回唯一记录的情况。
     *
     * @param conditions    待查询的条件。
     * @return              当前查询条件所匹配的记录。
     */
    public E queryUniqueByConditions(final Map<String, Object> conditions);
    
    
  
    /**
     * 功能描述：根据当前实体对象唯一标识值进行查询。<p>
     * 默认根据主键ID进行查询。
     *
     * @param id    当前实体主键。
     * @return      查询到的实体对象。
     */
    public E getById(final PK id);
    
    
    
    /**
     * 功能描述：统计所有记录数。
     *
     * @return  所有记录条数。
     */
    public Long countAll();
    
    /**
     * 功能描述：统计多条件查询记录数。
     *
     * @param conditions    当前查询条件。
     * @return              当前多条件查询返回的记录数。
     */
    public Long countByConditions(final Map<String, Object> conditions);
    
    /**
     * 功能描述：查询当前实体的某个属性值是否存在。
     *
     * @param property  当前属性名。
     * @param value     当前属性值。
     * @return          查询的属性值是否已存在。
     */
    public Boolean isExistByProperty(final String property, final Object value);
    
    /**
     * 
     * 功能描述：多条件查询结果集是否存在。
     *
     * @param conditions    待查询的条件。
     * @return              多条件查询记录是否存在。
     */
    public Boolean isExistByConditions(final Map<String, Object> conditions);
    
    
    
    /**
     * 功能描述：保存数据。
     *
     * @param entity  待保存的实体对象。
     * @return        保存成功的实体对象。
     */
    public E save(final E entity);
    
    
    /**
     * 功能描述：更新数据。<P>
     * 适用于根据主键更新。
     *
     * @param entity  待更新的实体对象。
     */
    public Boolean update(final E entity);
    
    /**  
     * 功能描述：根据多个条件更新数据。<p>
     * 适用于以多个属性作为更新条件的情况。
     *
     * @param updateProperties      待更新的属性集合。
     * @param updateConditions      更新的条件集合。
     * @return                      更新是否成功。
     */
    public Boolean updateByConditions(final Map<String, Object> updateProperties, final Map<String, Object> updateConditions);
    
    /**
     * 功能描述：根据某个属性更新记录。<P>
     * 适用于以单个属性作为更新条件的情况。
     *
     * @param updateProperties  待更新的属性集合。
     * @param property          更新条件的属性名称。
     * @param value             更新条件的属性值。
     * @return                  更新是否成功。
     */
    public Boolean updateByConditions(final Map<String, Object> updateProperties, final String property, final String value);
    
    
    
    /**
     * 功能描述：根据主键删除记录。
     */
    public Boolean removeById(final PK id);
    
    /**
     * 功能描述：根据某个属性删除记录。
     *
     * @param property  待删除记录属性名称。
     * @param value     待删除记录属性值。
     * @return          删除是否成功。
     */
    public Boolean removeByProperty(final String property, final String value);
    
    /**
     * 功能描述：根据条件删除记录。
     *
     * @param conditions   删除条件。 
     * @return             删除是否成功。
     */
    public Boolean removeByConditions(final Map<String, Object> conditions);
    
    /**
     * 功能描述：删除某个实体对象。
     *
     * @param entity       待保存的实体对象。
     * @return             删除是否成功。
     */
    public Boolean remove(final E entity);
}