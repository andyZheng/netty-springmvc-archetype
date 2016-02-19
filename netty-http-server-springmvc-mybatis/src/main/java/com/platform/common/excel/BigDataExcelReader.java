/*
 * 文件名：AbstractExcelReader.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.platform.common.excel.entity.Sheet;

/**
 * 功能描述： <code>BigDataExcelReader</code>为Excel大数据文件读取器。
 * <P>
 * 功能详细描述：目前主要针对Excel电子表格、Excel XML两个格式提供个性化的解决方案。
 * 
 * @author  andy.zheng0807@gmail.com
 * @version 1.0, 2013-7-29 下午4:55:08
 * @since   Commons Platform 1.0
 */
public class BigDataExcelReader extends SimpleExcelReader{

    /** 单元格数据分隔符 默认为逗号 */
    private String dataSeparator = ",";
    
    /**
     * 多个sheet表格数据。
     */
    private Map<String, Sheet> moreSheetDataList = new HashMap<String, Sheet>();
    
 
    /* 
     * @see com.platform.common.excel.listeners.DataRowHandler#handlerRow(int, String int, java.util.List)
     */
    @Override
    public void handlerRow(int sheetIndex, String sheetName, int currentRow, List<String> rowList) {
        // 判定当前表格索引是否注册
        String key = String.valueOf(sheetIndex);
        Sheet sheet =  this.moreSheetDataList.get(key);
        List<String> singleSheetDataList = null;
        if(null == sheet){
            sheet = new Sheet();
            sheet.setSheetIndex(sheetIndex);
            sheet.setSheetName(sheetName);
            singleSheetDataList = new ArrayList<String>();
            sheet.setDataRows(singleSheetDataList);
            this.moreSheetDataList.put(key, sheet);
        }else{
            singleSheetDataList = sheet.getDataRows();
        }
        
        if(null != rowList && !rowList.isEmpty()){
            StringBuffer rows = new StringBuffer();
            for (int i = 0; i < rowList.size(); i++) {
                String content = rowList.get(i);
                if(null != content){
                    content = content.replaceAll("\\" + dataSeparator, "");
                }
                rows.append(content);
                if(i < rowList.size() - 1){
                    rows.append(dataSeparator);
                }
            }
            
            String rowData = rows.toString();
            if(!rowData.trim().isEmpty()){
                //System.out.println(rowData);
                int index = singleSheetDataList.size();
                singleSheetDataList.add(index, rowData);
                sheet.setRows(singleSheetDataList.size());
            }
        }
    }

    /**
     * 功能说明：获取单个sheet表格。
     *
     * @return List<String> the singleSheetDataList 返回单个sheet表格。
     */
    @Override
    public Sheet getSingleSheetDataList(String sheetIndex) {
        Sheet currentSheet =  this.moreSheetDataList.get(sheetIndex);
        return currentSheet;
    }


    /**
     * 功能说明：获取多个sheet表格数据。
     *
     * @return Map<String,List<Sheet>> the moreSheetDataList 返回多个sheet表格。
     */
    @Override
    public Map<String, Sheet> getMoreSheetDataList() {
        return moreSheetDataList;
    }

    /**
     * 功能说明：设置单元格数据分隔符。
     *
     * @param dataSeparator 待设置的单元格数据分隔符。
     */
    public void setDataSeparator(String dataSeparator) {
        this.dataSeparator = dataSeparator;
    }
}
