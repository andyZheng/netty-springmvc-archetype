/*
 * 文件名：XSSFHandler.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel.parser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.xssf.eventusermodel.XSSFReader;
import org.apache.poi.xssf.model.SharedStringsTable;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

import com.platform.common.excel.handler.DataRowHandler;

/**
 * 功能描述：<code>XSSFHandler</code>为Excel XML格式（Excel 2007以上）的大文件提供解析处理方案。<p>
 * 功能详细描述：基于OPI用户事件模型，逐行解析Excel XML格式的大文件。
 *
 * @author   andy.zheng0807@gmail.com
 * @see      com.platform.common.excel.ExcelReaderFactory
 * @version  1.0, 2013-7-30 上午10:43:05
 * @since    Commons Platform 1.0
 */
public class XSSFParser extends DefaultHandler implements ExcelDataParser {
    
    /** 美式日期格式 */
    public final String DATE_FORMATE_STRING_US = "m/d/yy";

    /** Excel数据行处理监听器  */
    private DataRowHandler dataRowHanlder;
    
    /** 共享数据表格 */
    private SharedStringsTable sharedTable;
    
    /** 存储单元格内容 */
    private String contents;
    
    /** 判定单元格内容是否为字符类型 */
    private boolean nextIsString;

    /** 表格索引 */
    private int sheetIndex = -1;
    
    /** 表格名称 */
    private String sheetName;
    
    /** 数据行数据内容*/
    private List<String> rowList = null;
    
    /** 当前行 */
    private int currentRow = 0;
    
    /** 当前列 */
    private int currentColumn = 0;
    
    /** 样式表 */
    private StylesTable stylesTable;
    
    /** 中式日期格式 */
    private  String dateFormateString = "yyyy-MM-dd 00:00:00";
    
    /** 数据类型  默认为字符串类型 */
    private CellDataType dataType = CellDataType.STRING;
    
    /** 数据格式化 */
    private DataFormatter formatter = new DataFormatter();

    /** 数据格式化索引 */
    private short formatIndex;
    
    /** 是否包含空单元格 */
    private boolean blankCell = true;
    
    
    /**
     * 
     * 功能描述：重载构造函数。
     *
     */
    public XSSFParser(DataRowHandler dataRowHanlder){
        this.dataRowHanlder = dataRowHanlder;
        rowList = new ArrayList<String>();
    }
    
    /* 
     * @see com.platform.common.excel.parser.ExcelDataParser#parser(java.lang.String)
     */
    @Override
    public void parser(String fileName) throws Throwable {
        OPCPackage pkg = OPCPackage.open(fileName);
        XSSFReader r = new XSSFReader(pkg);
        this.sharedTable = r.getSharedStringsTable();
        this.stylesTable = r.getStylesTable();

        XMLReader parser = XMLReaderFactory.createXMLReader("org.apache.xerces.parsers.SAXParser");
        parser.setContentHandler(this);

        Iterator<InputStream> sheets = r.getSheetsData();
        while (sheets.hasNext()) {
            currentRow = 0;
            sheetIndex++;
            InputStream sheet = sheets.next();
            InputSource sheetSource = new InputSource(sheet);
            parser.parse(sheetSource);
            sheet.close();
        }
    }

    
    public void startElement(String uri, String localName, String name, Attributes attributes) throws SAXException {
        // c => 单元格
        if (name.equals("c")) {
            // Print the cell reference
            //System.out.print(attributes.getValue("r") + " - ");
            // 如果下一个元素是 SST 的索引，则将nextIsString标记为true
            String cellType = attributes.getValue("t");
            if (null != cellType) {
                nextIsString = true;
                dataType = CellDataType.STRING;
            }else {
                 String cellStyleStr = attributes.getValue("s");
                 if(null != cellStyleStr){
                    nextIsString = true;
                    int styleIndex = Integer.parseInt(cellStyleStr);
                    XSSFCellStyle style = stylesTable.getStyleAt(styleIndex);
                    formatIndex = style.getDataFormat();
                    String formateString = style.getDataFormatString();
                    if (DATE_FORMATE_STRING_US.equals(formateString))
                    {
                        dataType = CellDataType.DATE;
                    }else{
                        nextIsString = false;
                        if(blankCell){
                            rowList.add(currentColumn, " ");
                            currentColumn++;
                        }
                    }
                }
            }
        }
        // 置空
        contents = "";
    }

    public void endElement(String uri, String localName, String name) throws SAXException {
        // 根据SST的索引值的到单元格的真正要存储的字符串
        // 这时characters()方法可能会被调用多次
        if (nextIsString) {
            try {
                int idx = Integer.parseInt(contents);
                contents = new XSSFRichTextString(sharedTable.getEntryAt(idx)).toString();
            } catch (Exception e) {
            }
        }
        

        // v => 单元格的值，如果单元格是字符串则v标签的值为该字符串在SST中的索引
        // 将单元格内容加入rowlist中，在这之前先去掉字符串前后的空白符
        if (name.equals("v")) {
            String value = contents.trim();
            value = value.equals("") ? " " : value;
            if(dataType == CellDataType.DATE){
                value = formatter.formatRawCellContents(Double.parseDouble(value), formatIndex, dateFormateString);
                dataType = CellDataType.STRING;
            }
            
            // 数值类型无SST索引，覆盖初始插入的空值
            int lastIndex = currentColumn - 1;
            if(!nextIsString && rowList.get(lastIndex).trim().isEmpty()){
                currentColumn--;
            }
            
            rowList.add(currentColumn, value);
            currentColumn++;
        } else {
            // 如果标签名称为 row ，这说明已到行尾
            if (name.equals("row")) {
                dataRowHanlder.handlerRow(sheetIndex, sheetName, currentRow, rowList);
                    
                rowList.clear();
                currentRow++;
                currentColumn = 0;
            }
        }
        
        nextIsString = true;
    }

    public void characters(char[] ch, int start, int length) throws SAXException {
        // 得到单元格内容的值
        contents += new String(ch, start, length);
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
        return sheetIndex + 1;
    }
    
    static enum CellDataType{
        STRING,
        DATE;
    }
}
