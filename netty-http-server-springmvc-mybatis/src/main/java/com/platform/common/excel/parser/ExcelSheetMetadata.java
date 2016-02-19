/*
 * 文件名：ExcelSheetMetadata.java
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
 * 功能描述：<code>ExcelSheetMetadata</code>提供Excel文件单元格元数据信息。
 *
 * @author   andy.zheng0807@gmail.com
 * @version  1.0, 2013-7-31 下午2:22:27
 * @since    Commons Platform 1.0
 */
public interface ExcelSheetMetadata {

    /**
     * 功能说明：设置单元格日期格式。<p>
     * 缺省为：yyyy-MM-dd 00:00:00
     *
     * @param dateFormateString 待设置的单元格日期格式。
     */
    public abstract void setDateFormateString(String dateFormateString);

    /**
     * 功能说明：获取加载的Excel文件表格总数量。
     *
     * @return int the sheetIndex 返回加载的Excel文件表格总数量。
     */
    public abstract int getSheetCount();
}