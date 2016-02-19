/*
 * 文件名：BaseMyBatisDao.java
 * 版权：Copyright 2013-2015 ChengDu MS Leagues Studio. All Rights Reserved. 
 * 描述： 公共平台
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.mybatis.impl;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.platform.common.dao.mybatis.MyBatisDao;
import com.platform.common.entity.BaseEntity;

/**
 * 功能描述：<code>BaseMyBatisDao</code>是MyBatis数据访问实现类。
 * <p>
 * 本类继承自{@link MyBatis},实现了MyBatis常规数据访问接口。
 *
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2014年6月20日 上午10:14:31
 * @since Common-Platform/MyBatis DAO 1.0
 */
public abstract class BaseMyBatisDao<E extends BaseEntity<E, PK>, PK extends Serializable> extends SqlSessionDaoSupport
        implements MyBatisDao<E, PK> {

    /** 日志对象 */
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());

    /** 当前实体类 */
    private Class<E> entityClass;

    @SuppressWarnings("unchecked")
    public BaseMyBatisDao() {
        ParameterizedType type = (ParameterizedType) this.getClass().getGenericSuperclass();
        Type[] actualTypeArguments = type.getActualTypeArguments();
        this.entityClass = (Class<E>) actualTypeArguments[0];
    }

    @Override
    public List<E> queryAll() {
        return this.getSqlSession().selectList(this.getQueryStatement());
    }

    @Override
    public List<E> queryAll(final String sortBy) {
        return this.getSqlSession().selectList(this.getQueryStatement(), new HashMap<String, String>() {
            /**
             * 
             */
            private static final long serialVersionUID = 7950634558354180395L;

            {
                this.put("sortColumns", sortBy);
            }
        });
    }

    @Override
    public List<E> queryAll(final Integer offset, final Integer pageSize, final String... sortBy) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startRow", offset + 1);
        params.put("endRow", offset + pageSize);
        params.put("offset", offset);
        params.put("limit", pageSize);

        if (null != sortBy && sortBy.length == 1) {
            params = new HashMap<String, Object>();
            params.put("sortColumns", sortBy[0]);
        }

        return this.getSqlSession().selectList(this.getQueryStatement(), params);
    }

    @Override
    public List<E> queryByProperty(String property, Object value, String... sortBy) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(property, value);

        if (null != sortBy && sortBy.length == 1) {
            params.put("sortColumns", sortBy[0]);
        }

        return this.getSqlSession().selectList(this.getQueryStatement(), params);
    }

    @Override
    public List<E> queryByProperty(Integer offset, Integer pageSize, String property, Object value, String... sortBy) {
        Map<String, Object> params = new HashMap<String, Object>();

        params.put("startRow", offset + 1);
        params.put("endRow", offset + pageSize);
        params.put("offset", offset);
        params.put("limit", pageSize);

        params.put(property, value);

        if (null != sortBy && sortBy.length == 1) {
            params = new HashMap<String, Object>();
            params.put("sortColumns", sortBy[0]);
        }

        return this.getSqlSession().selectList(this.getQueryStatement(), params);
    }

    @Override
    public List<E> queryByConditions(Map<String, Object> conditions, String... sortBy) {
        if (null == conditions) {
            return null;
        }

        if (null != sortBy && sortBy.length == 1) {
            conditions = new HashMap<String, Object>();
            conditions.put("sortColumns", sortBy[0]);
        }

        return this.getSqlSession().selectList(this.getQueryStatement(), conditions);
    }

    @Override
    public List<E> queryByConditions(Integer offset, Integer pageSize, Map<String, Object> conditions, String... sortBy) {
        if (null == conditions) {
            return null;
        }

        conditions.put("startRow", offset + 1);
        conditions.put("endRow", offset + pageSize);
        conditions.put("offset", offset);
        conditions.put("limit", pageSize);

        if (null != sortBy && sortBy.length == 1) {
            conditions = new HashMap<String, Object>();
            conditions.put("sortColumns", sortBy[0]);
        }

        return this.getSqlSession().selectList(this.getQueryStatement(), conditions);
    }

    @Override
    public E queryByUniqueProperty(String property, Object value) {
        Map<String, Object> conditions = new HashMap<String, Object>();
        conditions.put(property, value);

        return this.getSqlSession().selectOne(this.getQueryStatement(), conditions);
    }

    @Override
    public E queryUniqueByConditions(Map<String, Object> conditions) {
        if (null == conditions) {
            return null;
        }

        return this.getSqlSession().selectOne(this.getQueryStatement(), conditions);
    }

    @Override
    public E getById(PK id) {
        return this.getSqlSession().selectOne(this.getFindByPrimaryKeyStatement(), id);
    }

    @Override
    public Long countAll() {
        Number number = (Number) this.getSqlSession().selectOne(this.getCountStatement());
        return number.longValue();
    }

    @Override
    public Long countByConditions(Map<String, Object> conditions) {
        Number number = (Number) this.getSqlSession().selectOne(this.getCountStatement(), conditions);
        return number.longValue();
    }

    @Override
    public Boolean isExistByProperty(String property, Object value) {
        List<E> queryByProperty = this.queryByProperty(property, value);
        if (null != queryByProperty && !queryByProperty.isEmpty()) {
            return true;
        }
        
        return false;
    }

    @Override
    public Boolean isExistByConditions(Map<String, Object> conditions) {
        List<E> queryByConditions = this.queryByConditions(conditions);
        if (null != queryByConditions && !queryByConditions.isEmpty()) {
            return true;
        }
        
        return false;
    }

    @Override
    public E save(E entity) {
        this.getSqlSession().insert(this.getInsertStatement(), entity);
        return entity;
    }

    @Override
    public Boolean update(E entity) {
        int resultId = this.getSqlSession().update(this.getUpdateStatement(), entity);
        return resultId > 0 ? true : false;
    }

    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, Map<String, Object> updateConditions) {
        throw new UnsupportedOperationException("The method has not implemented!");
    }

    @Override
    public Boolean updateByConditions(Map<String, Object> updateProperties, String property, String value) {
        throw new UnsupportedOperationException("The method has not implemented!");
    }

    @Override
    public Boolean removeById(final PK id) {
        int result = this.getSqlSession().delete(this.getDeleteStatement(), new HashMap<String, PK> () {
            /**
             * 
             */
            private static final long serialVersionUID = 5105405519789952593L;

            {
                this.put("id", id);
            }
        });
        
        return result > 0 ? true : false;
    }

    @Override
    public Boolean removeByProperty(final String property, final String value) {
        int result = this.getSqlSession().delete(this.getDeleteStatement(), new HashMap<String, String> () {
            /**
             * 
             */
            private static final long serialVersionUID = 5105405519789952593L;

            {
                this.put(property, value);
            }
        });
        
        return result > 0 ? true : false;
    }

    @Override
    public Boolean removeByConditions(Map<String, Object> conditions) {
        int result = this.getSqlSession().delete(this.getDeleteStatement(), conditions);
        return result > 0 ? true : false;
    }

    @Override
    public Boolean remove(E entity) {
        int result = this.getSqlSession().delete(this.getDeleteStatement(), entity);
        return result > 0 ? true : false;
    }

    /**
     * 功能描述：获取当前实体类型。
     *
     * @return 当前实体类型。
     */
    protected Class<?> getEntityClass() {
        return this.entityClass;
    }

    /**
     * 功能描述：获取当前实体Mybatis Mapper的命名空间。
     *
     * @return 当前实体Mybatis Mapper的命名空间。
     */
    protected String getMybatisMapperNamesapce() {
        return this.entityClass.getSimpleName();
    }

    /**
     * 功能描述：获取全限定查询语句名称。 全限定查询语句名称由【命名空间+查询语句名称】组成。
     * 
     * @param custormQueryStatement
     *            自定义查询语句名称。
     * @return 当前全限定查询语句名称。
     */
    protected String getQueryStatement(String... custormQueryStatement) {
        return this.builderQueryString(".findPage", custormQueryStatement);
    }

    /**
     * 功能描述：获取全限定计数查询语句名称。
     *
     * @param custormQueryStatement
     *            自定义查询语句名称。
     * @return 全限定计数查询语句名称。
     */
    protected String getCountStatement(String... custormQueryStatement) {
        return this.builderQueryString(".count", custormQueryStatement);
    }

    /**
     * 功能描述：获取全限定主键查询语句名称。
     * 
     * @param custormQueryStatement
     *            自定义查询语句名称。
     * @return 全限定主键查询语句名称。
     */
    protected String getFindByPrimaryKeyStatement(String... custormQueryStatement) {
        return this.builderQueryString(".getById", custormQueryStatement);
    }

    private String builderQueryString(String defaultStatement, String... queryStatement) {
        if (null != queryStatement && queryStatement.length == 0) {
            defaultStatement = queryStatement[0];
        }

        return this.getMybatisMapperNamesapce() + defaultStatement;
    }

    /**
     * 功能描述：获取全限定插入操作语句名称。
     *
     * @param customUpdaterStatement
     *            自定义更新语句名称。
     * @return 全限定插入操作语句名称。
     */
    protected String getInsertStatement(String... customUpdaterStatement) {
        return this.builderUpdaterStatement(".insert", customUpdaterStatement);
    }

    /**
     * 功能描述：获取全限定更新操作语句名称。
     *
     * @param customUpdaterStatement
     *            自定义更新语句名称。
     * @return 全限定更新操作语句名称。
     */
    protected String getUpdateStatement(String... customUpdaterStatement) {
        return this.builderUpdaterStatement(".update", customUpdaterStatement);
    }

    /**
     * 功能描述：获取全限定删除操作语句名称。
     *
     * @param customUpdaterStatement
     *            自定义更新语句名称。
     * @return 全限定删除操作语句名称。
     */
    protected String getDeleteStatement(String... customUpdaterStatement) {
        return this.builderUpdaterStatement(".delete", customUpdaterStatement);
    }

    private String builderUpdaterStatement(String defaultUpdaterStatement, String... customUpdaterStatement) {
        if (null != customUpdaterStatement && customUpdaterStatement.length == 0) {
            defaultUpdaterStatement = customUpdaterStatement[0];
        }

        return this.getMybatisMapperNamesapce() + defaultUpdaterStatement;
    }

}
