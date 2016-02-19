/*
 * 文件名：page.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.paging;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 功能描述：<code>Page</code>类用于定义分页实体类。
 * <p>
 * 本分页实体类包含分页的常规参数有:
 * <ul>
 * <li>{@link currentPage}-当前页码</li>
 * <li>{@link pageSize}-每页记录数</li>
 * <li>{@link totalCount}-总记录数</li>
 * <li>{@link offset}-当前页起始游标</li>
 * <li>{@link totalPages}-总页数</li>
 * <li>{@link resultList}-当前页结果集</li>
 * <li>{@link filters}-查询参数</li>
 * 
 * @author andy.zheng andy.zheng0807@gmail.com
 * @version 1.0, 2014年4月3日 上午10:35:41
 * @since Common-Platform/数据访问组件 1.0
 */
public class Page implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    /** 当前页码 缺省为1 */
    private Integer currentPage = 1;

    /** 每页记录数 缺省为10 */
    private Integer pageSize = 10;
    
    
    /** 起始游标 */
    private Integer offset;

    /** 总页数 */
    private Integer totalPages;
    
    /** 总记录数 */
    private Long totalCount;

    /** 当前结果集 */
    private List<?> resultList;

    /** 查询参数 */
    private Map<String, Object> filters = new HashMap<String, Object>();

    /**
     * 功能描述：重载构造函数。
     * 
     * @param currentPage
     *            当前页码。
     * @param pageSize
     *            每页记录数。
     */
    public Page(Integer currentPage, Integer pageSize) {
        if (null != currentPage && currentPage.intValue() > 0) {
            this.currentPage = currentPage;
        }

        if (null != pageSize && pageSize.intValue() > 0) {
            this.pageSize = pageSize;
        }
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        if (null != currentPage && currentPage.intValue() > 0) {
            this.currentPage = currentPage;
        }
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        if (null != pageSize && pageSize.intValue() > 0) {
            this.pageSize = pageSize;
        }
    }

    public Integer getOffset() {
        this.offset = (this.currentPage.intValue() - 1) * this.pageSize.intValue();

        return this.offset;
    }

    public Integer getTotalPages() {
        if (null != this.totalCount) {
            long value = this.totalCount.longValue() / this.pageSize.intValue();
            int number = Long.valueOf(value).intValue();
            if (0 != this.totalCount.longValue() % this.pageSize.intValue()) {
                number += 1;
            }
            this.totalPages = number;
        }

        return totalPages;
    }

    public Long getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(Long totalCount) {
        this.totalCount = totalCount;
    }

    public List<?> getResultList() {
        return resultList;
    }

    public void setResultList(List<?> resultList) {
        this.resultList = resultList;
    }

    public Map<String, Object> getFilters() {
        return filters;
    }

    public void setFilters(Map<String, Object> filters) {
        this.filters = filters;
    }

}