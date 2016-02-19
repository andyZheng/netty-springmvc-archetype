/*
 * 文件名：ConfigurationKey.java
 * 版权：Copyright 2012-2013 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.config;


/**
 * 功能描述：<code>ConfigurationKey</code>提供配置项key容器。
 * <p>
 * 功能详细描述：本类管理配置项key全局配置，所声明的常量与<b>config.properties</b>配置文件中的key一一对应。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2013-3-29 下午1:41:24
 * @since    Common-Platform/Config Component 1.0 
 */
public final class ConfigurationKey {

    /** 员工薪酬文件所在路径 */
    public final static String EMPLOYEE_SALARY_FILE_PATH = "app.employee.salary.file.path";
    
    /** 当前客户端任务工作线程数配置key */
    public final static String CLIENT_WORK_THREAD_AMOUNT = "app.client.work.thread.count";
    
    /** 邮件服务器地址key  */
    public final static String EMAIL_SERVER_HOST = "mail.server.host";
   
    /** 邮件服务器端口key*/
    public final static String EMAIL_SERVER_PORT = "mail.server.port";
    
    /** 邮件服务器认证key */
    public final static String EMAIL_SERVER_AUTH = "mail.server.auth";
    
    /** 认证用户名key */
    public final static String EMAIL_SERVER_USER = "mail.server.user";
    
    /** 认证用户密码key */
    public final static String EMAIL_SERVER_PASSWORD = "mail.server.password";
    
    /** 邮件发件人key*/
    public final static String EMAIL_FROM_PERSON = "mail.from.person";
    
	/** 邮件内容 */
	public static final String EMAIL_CONTENT_INFO = "mail.content.info";
    
    /**	文件加密盐值 */
    public final static String FILE_ENCRYPT_SALT = "app.client.file.encrypt.salt";

    /** 开启Debug模式 */
	public static final String ENABLE_DUBUG_MODEL = "app.client.enable.debug.model";
	
	/** Mongo数据库主机Key */
    public static final String MONGO_DB_HOST_KEY = "db.mongo.host";
    
    /** Mongo数据库端口Key */
    public static final String MONGO_DB_PORT_KEY = "db.mongo.port";
    
    /** Mongo数据库名Key */
    public static final String MONGO_DB_NAME_KEY = "db.mongo.dbname";
    
    /** Mongo数据库连接用户名Key */
    public static final String MONGO_USER_NAME_KEY = "db.mongo.username";
    
    /** Mongo数据库连接密码Key  */
    public static final String MONGO_PASSWORD_KEY = "db.mongo.password";
    
    /** Mongo数据库连接密码Key  */
    public static final String MONGO_CONNECTIONS_KEY = "db.mongo.connectionsPerHost";
    
    /** Mongo数据库连接密码Key  */
    public static final String MONGO_THREADS_KEY = "db.mongo.threadsAllowedToBlockForConnectionMultiplier";
    
    /** 工作线程数量Key */
    public static final String WORK_THREAD_COUNT = "app.work.thread.count";
}
