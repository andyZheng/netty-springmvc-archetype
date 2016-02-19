/*
 * 文件名：Sheet.java
 * 版权：Copyright 2012-2013 SOHO studio. All Rights Reserved. 
 * 描述： 公共平台V1.0
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 功能描述：<code>Sheet</code>为单元格实体对象。
 *
 * @author   andy.zheng0807@gmail.com
 * @see      com.platform.common.excel.BigDataExcelReader
 * @version  1.0, 2013-8-8 上午11:38:27
 * @since    Commons Platform 1.0
 */
public class Sheet implements Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -5474928151185917069L;
    
    /** 表格索引 */
    private int sheetIndex;
    
    /** 表格名称 */
    private String sheetName;
    
    /** 数据总行数 */
    private int rows;
    
    /** 数据行  */
    private List<String> dataRows;

    /**
     * 功能说明：获取表格索引
     *
     * @return int the sheetIndex 返回表格索引
     */
    public int getSheetIndex() {
        return sheetIndex;
    }

    /**
     * 功能说明：设置表格索引
     *
     * @param sheetIndex 表格索引
     */
    public void setSheetIndex(int sheetIndex) {
        this.sheetIndex = sheetIndex;
    }

    /**
     * 功能说明：获取表格名称
     *
     * @return String the sheetName 返回表格名称
     */
    public String getSheetName() {
        return sheetName;
    }

    /**
     * 功能说明：设置表格名称
     *
     * @param sheetName 表格名称
     */
    public void setSheetName(String sheetName) {
        this.sheetName = sheetName;
    }

    /**
     * 功能说明：获取数据总行数
     *
     * @return int the rows 数据总行数
     */
    public int getRows() {
        return rows;
    }

    /**
     * 功能说明：设置数据总行数
     *
     * @param rows 数据总行数
     */
    public void setRows(int rows) {
        this.rows = rows;
    }
    
    /**
     * 功能说明：获取数据总数
     *
     * @return int the rows 数据总数
     */
    public List<String> getDataRows() {
        return dataRows;
    }

    /**
     * 功能说明：设置数据总数
     *
     * @param rows 数据总数
     */
    public void setDataRows(List<String> dataRows) {
        this.dataRows = dataRows;
    }
    
    /* 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        StringBuffer printString = new StringBuffer();
        printString.append("[");
        printString.append("\n\tsheetIndex = ").append(this.sheetIndex);
        printString.append(",\n\tsheetName = ").append(this.sheetName);
        printString.append(",\n\trows = ").append(this.rows);
        printString.append(",\n\tdataRows = {");
        for (int i = 0; i < this.dataRows.size(); i++) {
            String dataRow = this.dataRows.get(i);
            printString.append("\n\t\t").append(dataRow);
            if(i == this.dataRows.size() - 1){
                printString.append("\n\t}");
            }
        }
        printString.append("\n]");
        
        return printString.toString();
    }
}
