/*
 * 文件名：MyBatisDao.java
 * 版权：Copyright 2013-2015 ChengDu MS Leagues Studio. All Rights Reserved. 
 * 描述： 公共平台
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.dao.mybatis;

import java.io.Serializable;

import com.platform.common.dao.CommonDao;

/**
 * 功能描述：<code>MyBatisDao</code>基于MyBatis定义Dao层数据访问接口。
 * <p>本类扩展自{@link CommonDao},将根据MyBatis的数据访问特性定义个性化数据访问接口。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年6月20日 上午10:07:37
 * @since    Common-Platform/MyBatis DAO 1.0
 */
public interface MyBatisDao<E extends Serializable, PK extends Serializable> extends CommonDao<E, PK> {

}
