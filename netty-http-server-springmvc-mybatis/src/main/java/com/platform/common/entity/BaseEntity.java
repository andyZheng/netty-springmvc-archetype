/*
 * 文件名：BaseEntity.java
 * 版权：Copyright 2012-2014 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.entity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;

import com.platform.common.utils.StringUtils;

/**
 * 功能描述：<code>BaseEntity</code>类用于定义实体基类。
 * <p>
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2014年4月3日 上午9:13:39
 * @since    Common-Platform/Entity Component 1.0
 */
public class BaseEntity<E, PK> implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1504516580023461232L;
    
    /** 日期格式 */
    public static final String DATE_FORMAT = "yyyy-MM-dd";
    
    /** 年月格式 */
    public static final String DATE_FORMAT_MONTH = "yyyy-MM";

    /** 时间格式 */
    public static final String TIME_FORMAT = "HH:mm:ss";

    /** 日期时间格式 */
    public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    /** 时间戳格式 */
    public static final String TIMESTAMP_FORMAT = "yyyy-MM-dd HH:mm:ss.S";
    
    /** 截取内容后面的符号 默认为省略号 */
    public static final String ELLIPSIS_SYMBOL = "...";
    
    /** 日期格式化缓冲器 */
    private static Map<String, SimpleDateFormat> date_format_cache = new HashMap<String, SimpleDateFormat>();
    
    /**
     * 小数格式化对象缓存器
     */
    private static  Map<DecimalFormatter , DecimalFormat> decial_format_cache = new HashMap<DecimalFormatter , DecimalFormat>();
    
    
    /**
     * 功能描述：将日期对象格式化为指定格式。
     *
     * @param date          待格式化的日期。
     * @param dateFormat    待格式化的日期。
     * @return              格式化后的字符串。
     */
    public static String date2String(final Date date,final String dateFormat) {
        SimpleDateFormat format = date_format_cache.get(dateFormat);
        if(null == format){
            format = new SimpleDateFormat(dateFormat);
            date_format_cache.put(dateFormat, format);
        }
        
        return format.format(date);
    }

    /**
     * 功能描述：将字符串格式日期转化为日期对象。
     *
     * @param dateString        字符串格式日期。
     * @param dateFormat        日期格式。
     * @return                  转化后的日期对象。
     * @throws ParseException 
     */
    public static Date string2Date(final String dateString,final String dateFormat) throws ParseException {
        SimpleDateFormat format = date_format_cache.get(dateFormat);
        if(null == format){
            format = new SimpleDateFormat(dateFormat);
            date_format_cache.put(dateFormat, format);
        }
        
        return format.parse(dateString);
    }
    
    /**
     * 提供保留小数点后几位接口
     * @param number        需要格式化的Number对象
     * @param numberFormat  小数格式化类型
     *        DecimalFormatter.NUMBER_FORMAT_ONE   保留小数点后一位
     *        DecimalFormatter.NUMBER_FORMAT_TWO   保留小数点后两位
     *        DecimalFormatter.NUMBER_FORMAT_THREE 保留小数点后三位
     * @return
     */
    public static String numberToString(final Object number, final DecimalFormatter decimalFormatter){
        DecimalFormat decimalFormat = decial_format_cache.get(decimalFormatter);
        if(null == decimalFormat){
             decimalFormat = new DecimalFormat(decimalFormatter.getValue());
             decial_format_cache.put(decimalFormatter, decimalFormat);
        }
        
        return decimalFormat.format(number);
    }
    
    /**
     * 功能描述：截取内容。
     * 适用于只显示固定长度的内容，忽略剩余内容的情况。
     *
     * @param content       待截取的内容。
     * @param maxLength     显示最大长度。
     * @param symbol        剩余内容显示的符号。
     * @return              截取后的内容。
     */
    public static String formatContent(String content, int maxLength, String ... symbol){
        if(!StringUtils.isEmpty(content)){
            int length = content.length();
            if(length > maxLength){
                String currentSymbol = ELLIPSIS_SYMBOL;
                if(null != symbol && symbol.length == 1){
                    currentSymbol = symbol[0];
                }
                content = content.substring(0, maxLength) + currentSymbol;
            }
        }
        
        return content;
    }
    
    /* 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this, ToStringStyle.MULTI_LINE_STYLE);
    }
    
    public static  enum DecimalFormatter{
        /**
         * 保留到小数点后面一位
         */
        DECIMAL_FORMAT_ONE("######.#"),
        
        /**
         * 保留到小数点后面两位
         */
        DECIMAL_FORMAT_TWO("######.##"),
        
        /**
         * 保留到小数点后面三位
         */
        DECIMAL_FORMAT_THREE ("######.###");
        
        private String value;
        
        private DecimalFormatter(String value){
            this.value = value;
        }
        
        public String getValue() {
            return value;
        }
        
        public void setValue(String value) {
            this.value = value;
        }
    }
}