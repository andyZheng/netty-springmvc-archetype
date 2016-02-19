/*
 * 文件名：ServerConfig.java
 * 版权：Copyright 2013-2015 ChengDu MS Leagues Studio. All Rights Reserved. 
 * 描述： 游戏服务器框架
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.soho.framework.server.config;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 功能描述：<code>ConfigurationReader</code>类是配置项读取器。
 * <p>
 * 功能详细描述：系统加载时，本读取器将加载<b>server.properties</b>配置文件并读取所有配置项缓存到系统内存中。
 * 本类为单例实例，获取配置项示例代码如下：
 * 
 * <pre>
 * ServerConfigReader configReader = ServerConfigReader.getInstance();
 * String appServerUrl = configReader.getString(&quot;app.server.port&quot;);
 * </pre>
 *
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2013-3-29 下午1:49:47
 * @since Game Server Framework/Configuration 1.0
 */
public final class ServerConfigReader {

    /** Solr客户端配置路径 */
    private final static String BASE_NAME = "server";

    /** 系统配置文件读取器 */
    private static ServerConfigReader configReader = new ServerConfigReader();

    /** 配置属性集合 */
    private ResourceBundle resourceBundle = null;

    /**
     * 私有构造器
     */
    private ServerConfigReader() {
        resourceBundle = ResourceBundle.getBundle(BASE_NAME, Locale.getDefault());
    }

    /**
     * 获取配置文件读取器对象
     * 
     * @return 配置文件读取器对象
     */
    public static ServerConfigReader getInstance() {
        return configReader;
    }

    /**
     * 根据key获取值
     * 
     * 值类型为字符串
     * 
     * @param key
     *            配置键
     * @return 字符类型的值
     */
    public String getString(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        try {
            return resourceBundle.getString(key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    /**
     * 根据key获取值
     * 
     * 值类型为Int数值类型
     * 
     * @param key
     *            配置键
     * @return Int数值类型的值
     */
    public Integer getInt(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        return Integer.valueOf(this.getString(key));
    }

    /**
     * 根据key获取值
     * 
     * 值类型为Boolean类型
     * 
     * @param key
     *            配置键
     * @return Boolean类型的值
     */
    public Boolean getBoolean(String key) {
        if (null != key && "".equals(key)) {
            return null;
        }
        return Boolean.valueOf(this.getString(key));
    }
}
