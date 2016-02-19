/*
 * 文件名：StringUtils.java
 * 版权：Copyright 2012-2013 SOHO League. All Rights Reserved. 
 * 描述： 公共平台程序
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.utils;

import java.text.MessageFormat;

/**
 * 功能描述：<code>StringUtils</code>为字符串处理常用工具类，扩展自{@link org.apache.commons.lang.StringUtils}。
 *
 * @author   andy.zheng
 * @version  1.0, 2013-12-27 上午9:37:13
 * @since    CommonPlatform 1.0
 */
public class StringUtils extends org.apache.commons.lang.StringUtils {

    /**
     * 格式化字符串
     * 
     * 更多匹配模式参见java.text.MessageFormat
     * @see java.text.MessageFormat
     * 
     * @param pattern       匹配模式 
     *      eg. This is a {0}.
     * @param arguments     参数      
     *      eg. apple
     * @return              格式化后的字符串
     *      eg. This is a apple.
     */
    public static String format(String pattern , Object... arguments){
        return MessageFormat.format(pattern, arguments);
    }
}
