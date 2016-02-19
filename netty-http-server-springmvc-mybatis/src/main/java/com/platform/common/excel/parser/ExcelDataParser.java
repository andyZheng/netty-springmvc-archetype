/*
 * 文件名：ExcelDataParser.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel.parser;

/**
 * 功能描述：<code>ExcelDataParser</code>提供解析接口定义。
 *
 * @author   andy.zheng0807@gmail.com
 * @see      com.platform.common.excel.parser.HSSFParser
 * @see      com.platformcommon.excel.parser.XSSFParser
 * @version  1.0, 2013-7-30 上午10:26:32
 * @since    Commons Platform 1.0
 */
public interface ExcelDataParser extends ExcelSheetMetadata {

    /**
     * 
     * 功能描述： Excel文件解析器。 
     * 
     * @param fileName  待解析的Excel文件。
     * @throws Throwable 
     */
    public void parser(String fileName) throws Throwable;
}
