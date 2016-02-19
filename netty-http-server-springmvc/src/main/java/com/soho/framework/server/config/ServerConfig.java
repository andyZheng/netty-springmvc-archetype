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

import io.netty.util.internal.SystemPropertyUtil;

import java.nio.charset.Charset;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 功能描述：<code>ServerConfig</code>为服务器提供全局配置信息。</p>
 *
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2015年4月28日 上午9:49:36
 * @since Game Server Framework/Configuration 1.0
 */
public final class ServerConfig {

    /** 日志对象 */
    final static Logger logger = LoggerFactory.getLogger(ServerConfig.class);

    /** 服务器协议 缺省为http */
    private static String protocol = "http";

    /** 服务器端口 缺省为8080 */
    private static int port = 8080;

    /** 业务线程池中线程数。缺省为CPU核数 * 4 */
    private static int businessThreadNumber;

    /** https */
    private static boolean ssl = false;

    /** Session超时时间 默认为30分钟 */
    private static int sessionTimeout = 30;

    /** 服务器编解码集 */
    private static String charsetName = "UTF-8";

    private static Charset charset;

    static {
        // 加载当前服务器配置信息
        ServerConfigReader configReader = ServerConfigReader.getInstance();

        // 获取业务线程数
        int availableProcessors = Runtime.getRuntime().availableProcessors();
        logger.info("The server cpu processors: {}", availableProcessors);
        businessThreadNumber = SystemPropertyUtil.getInt("rapid.server.businessThreadNumber", availableProcessors * 4);

        // 获取SSL配置
        Boolean isSSL = configReader.getBoolean("netty.http.server.ssl");
        ssl = isSSL;
        if (ssl) {
            port = 8443;
        }

        // 获取当前服务器端口配置
        Integer currentPort = configReader.getInt("netty.http.server.port");
        if (null != currentPort && currentPort.intValue() > 0) {
            port = currentPort;
        }

        // 获取当前服务器编解码集
        String currentCharsetName = configReader.getString("netty.http.server.charset.name");
        if (null != currentCharsetName && !currentCharsetName.isEmpty()) {
            charsetName = currentCharsetName;
        }
        
        // Session 
        String val = System.getProperty("netty.http.sessionTimeout");
        if (null != val) {
            try {
                int sessionTimeout = Integer.valueOf(val);
                ServerConfig.setSessionTimeout(sessionTimeout);
            } catch (Exception e) {
                logger.error("Parse port fail!", e);
            }
        }
    }

    public static void setPort(int port) {
        ServerConfig.port = port;
    }

    public static int getPort() {
        return port;
    }

    public static boolean isSsl() {
        return ssl;
    }

    public static void setSsl(boolean ssl) {
        ServerConfig.ssl = ssl;
    }

    public static String getProtocol() {
        return protocol;
    }

    public static int getBusinessThreadNumber() {
        return businessThreadNumber;
    }

    public static void setBusinessThreadNumber(int businessThreadNumber) {
        ServerConfig.businessThreadNumber = businessThreadNumber;
    }

    public static Charset getCharset() {
        charset = Charset.forName(charsetName);

        return charset;
    }

    public static String getCharsetName() {
        return charsetName;
    }

    public static void setCharsetName(String charsetName) {
        ServerConfig.charsetName = charsetName;
    }

    public static int getSessionTimeout() {
        return sessionTimeout;
    }

    public static void setSessionTimeout(int sessionTimeout) {
        ServerConfig.sessionTimeout = sessionTimeout;
    }

    public static String getServerName() {
        return "Netty-Http-Server";
    }
}
