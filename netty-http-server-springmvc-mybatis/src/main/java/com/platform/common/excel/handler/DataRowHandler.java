/*
 * 文件名：DataRowListeners.java
 * 版权：Copyright 2012-2013 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel.handler;

import java.util.List;

/**
 * 功能描述：<code>DataRowListener</code>提供数据行处理接口。<p>
 * 功能详细描述：本接口调用者可以自定义实现，解析器会自动回调。
 *
 * @author   andy.zheng0807@gmail.com
 * @see      com.platform.common.excel.BigDataExcelReader
 * @version  1.0, 2013-7-30 下午5:54:51
 * @since    Commons Platform 1.0
 */
public interface DataRowHandler {

    /**
     * 
     * 功能描述：逐行处理解析到数据。<p>
     * 功能详细描述：excel记录行操作方法，以sheet索引，行索引和行元素列表为参数，对sheet的一行元素进行操作，元素为String类型。
     * 
     * @param sheetIndex    当前表格索引
     * @param sheetName     当前表格名称
     * @param currentRow    当前行索引  
     * @param rowlist       当前行数据
     */
     abstract void handlerRow(int sheetIndex, String sheetName, int currentRow, List<String> rowlist);
}
