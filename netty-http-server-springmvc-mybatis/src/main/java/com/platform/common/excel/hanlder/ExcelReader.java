/*
 * 文件名：ExcelDataHandler.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel.hanlder;

import java.util.Map;

import com.platform.common.excel.entity.Sheet;
import com.platform.common.excel.parser.ExcelSheetMetadata;


/**
 * 功能描述：<code>ExcelReader</code>提供Excel文件解析高层接口定义。
 * 
 * @author  andy.zheng0807@gmail.com
 * @version 1.0, 2013-7-30 上午9:50:32
 * @since   Commons Platform 1.0
 */
public interface ExcelReader extends ExcelSheetMetadata{

    /**
     * 功能描述： 加载Excel文件。<p>
     * 功能详细描述：根据加载的Excel文件版本，自动匹配相应的解析器。
     * 
     * @param fileName  待加载的Excel文件。
     * @throws Throwable 
     */
    public abstract void load(String fileName) throws Throwable;
    
    /**
     * 功能说明：获取单个sheet表格数据。
     *
     * @param sheetIndex    表格所在索引位置。
     * @return Sheet the singleSheetDataList 返回单个sheet表格。
     */
    public Sheet getSingleSheetDataList(String sheetIndex);

    /**
     * 功能说明：获取多个sheet表格数据。
     *
     * @return Map<String,Sheet> the moreSheetDataList 返回多个sheet表格。
     */
    public Map<String, Sheet> getMoreSheetDataList();
    
    /**
     * 功能说明：设置单元格数据分隔符。
     *
     * @param dataSeparator 待设置的单元格数据分隔符。
     */
    public void setDataSeparator(String dataSeparator);
}
