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

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.Map;

import com.platform.common.excel.entity.Sheet;
import com.platform.common.excel.handler.DataRowHandler;
import com.platform.common.excel.handler.ExcelReader;
import com.platform.common.excel.parser.ExcelDataParser;
import com.platform.common.excel.parser.HSSFParser;
import com.platform.common.excel.parser.XSSFParser;

import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * 功能描述： <code>AbstractExcelReader</code>为Excel大数据文件读取器抽象类。
 * <P>
 * 功能详细描述：目前主要针对Excel电子表格、Excel XML两个格式提供个性化的解决方案。
 * 
 * @author  andy.zheng0807@gmail.com
 * @version 1.0, 2013-7-29 下午4:55:08
 * @since   Commons Platform 1.0
 */
public abstract class AbstractExcelReader implements ExcelReader, DataRowHandler{

    /** 中式日期格式 */
    private  String dateFormateString = "yyyy-MM-dd 00:00:00";
    
    /** 表格总数 */
    private int sheetCount = 1;
    
    /**
     * 功能描述： 加载Excel文件。<p>
     * 功能详细描述：根据加载的Excel文件版本，自动匹配相应的解析器。
     * 
     * @param fileName  待加载的Excel文件。
     * @throws Throwable 
     */
    public void load(String fileName) throws Throwable {
        ExcelDataParser parser = null;
        InputStream inp = new FileInputStream(fileName);
        if (!inp.markSupported())
            inp = new PushbackInputStream(inp, 8);
        if (POIFSFileSystem.hasPOIFSHeader(inp)) {
            parser = new HSSFParser(this);
        } else if (POIXMLDocument.hasOOXMLHeader(inp)) {
            parser = new XSSFParser(this);
        }
        if (null == parser) {
            throw new IllegalArgumentException("Your InputStream was neither an OLE2 stream, nor an OOXML stream");
        }

        parser.setDateFormateString(dateFormateString);
        parser.parser(fileName);
        
        this.sheetCount = parser.getSheetCount();
   }
    

    /**
     * 功能说明：设置单元格日期格式。<p>
     * 缺省为：yyyy-MM-dd 00:00:00
     *
     * @param dateFormateString 待设置的单元格日期格式。
     */
    @Override
    public void setDateFormateString(String dateFormateString) {
        this.dateFormateString = dateFormateString;
    }

    /**
     * 功能说明：获取加载的Excel文件表格总数量。
     *
     * @return int the sheetIndex 返回加载的Excel文件表格总数量。
     */
    @Override
    public int getSheetCount() {
        return sheetCount;
    }

    /* 
     * @see com.platform.common.excel.handler.ExcelReader#getSingleSheetDataList(java.lang.String)
     */
    @Override
    public Sheet getSingleSheetDataList(String sheetIndex) {
        return null;
    }

    /* 
     * @see com.platform.common.excel.handler.ExcelReader#getMoreSheetDataList()
     */
    @Override
    public Map<String, Sheet> getMoreSheetDataList() {
        return null;
    }

    /* 
     * @see com.platform.common.excel.handler.ExcelReader#setDataSeparator(java.lang.String)
     */
    @Override
    public void setDataSeparator(String dataSeparator) {
    }
}
