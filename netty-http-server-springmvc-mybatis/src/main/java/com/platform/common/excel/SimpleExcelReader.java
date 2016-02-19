/*
 * 文件名：simpleExcelReader.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel;

import java.util.List;

import com.platform.common.excel.listeners.DataRowListener;

/**
 * 功能描述： <code>SimpleExcelReader</code>为Excel大数据文件简单读取器。
 * <P>
 * 功能详细描述：目前主要针对Excel电子表格、Excel XML两个格式提供个性化的解决方案。
 * 
 * @author  andy.zheng0807@gmail.com
 * @version 1.0, 2013-7-29 下午4:55:08
 * @since   Commons Platform 1.0
 */
public  class SimpleExcelReader extends AbstractExcelReader {
    
    /** 数据行监听器 */
    private DataRowListener dataRowListener;

    /* 
     * @see com.platform.common.excel.listeners.DataRowHandler#handlerRow(int, java.lang.String, int, java.util.List)
     */
    @Override
    public void handlerRow(int sheetIndex, String sheetName, int currentRow, List<String> rowlist) {
        if(null != dataRowListener){
            dataRowListener.parseDataRow(sheetIndex, sheetName, currentRow, rowlist);
        }
    }
    
    /**
     * 功能说明：设置数据行处理监听器
     *
     * @param dataRowListener 数据行处理监听器
     */
    public void setDataRowListener(DataRowListener dataRowListener) {
        this.dataRowListener = dataRowListener;
    }

}
