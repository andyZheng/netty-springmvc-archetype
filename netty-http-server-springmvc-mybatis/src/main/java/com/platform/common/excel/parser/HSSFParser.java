/*
 * 文件名：AbstractHxlsHandler.java
 * 版权：Copyright 2012-2013 Beijing Founder Apabi Tech. Co. Ltd. All Rights Reserved. 
 * 描述： 
 * 修改人： 
 * 修改时间：
 * 跟踪单号：
 * 修改单号：
 * 修改内容：
 */
package com.platform.common.excel.parser;

import java.io.FileInputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.poi.hssf.eventusermodel.EventWorkbookBuilder.SheetRecordCollectingListener;
import org.apache.poi.hssf.eventusermodel.FormatTrackingHSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.eventusermodel.MissingRecordAwareHSSFListener;
import org.apache.poi.hssf.eventusermodel.dummyrecord.LastCellOfRowDummyRecord;
import org.apache.poi.hssf.eventusermodel.dummyrecord.MissingCellDummyRecord;
import org.apache.poi.hssf.model.HSSFFormulaParser;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BlankRecord;
import org.apache.poi.hssf.record.BoolErrRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.FormulaRecord;
import org.apache.poi.hssf.record.LabelRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NoteRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.RKRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.StringRecord;
import org.apache.poi.hssf.usermodel.HSSFDataFormatter;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

import com.platform.common.excel.handler.DataRowHandler;


/**
 * 功能描述：<code>XSSFHandler</code>为Excel电子格式（Excel 2003）的大文件提供解析处理方案。<p>
 * 功能详细描述：基于OPI用户事件模型，逐行解析Excel电子格式的大文件。
 *
 * @author   andy.zheng0807@gmail.com
 * @see      com.platform.common.excel.ExcelReaderFactory
 * @version  1.0, 2013-7-30 上午10:14:20
 * @since    Commons Platform 1.0
 */
public  class HSSFParser implements ExcelDataParser, HSSFListener {

    /** 美式日期格式 */
    public final String DATE_FORMATE_STRING_US = "m/d/yy";
    
    /** 中式日期格式 */
    private  String dateFormateString = "yyyy-MM-dd 00:00:00";
    
    /** 数据格式化对象 */
    private HSSFDataFormatter formatter = new HSSFDataFormatter();
    
    /** Excel数据处理器 */
    private DataRowHandler dataRowHanlder;
    
    /** 格式追踪监听器 */
    private FormatTrackingHSSFListener formatListener;
    
    /** 公式解析监听器 */
    private SheetRecordCollectingListener workbookBuildingListener;
    
    /** 待处理记录对象 */
    private SSTRecord sstRecord;
    
    /** 是否输出公式 */
    private boolean outputFormulaValues = true;
    
    /** 表格索引 */
    private int sheetIndex = -1;
    
    /** 数据行数据内容*/
    private List<String> rowList = null;
    
    /** 当前行 */
    private int currentRow = 0;
    
    /** 是否包含空单元格 */
    private boolean blankCell = true;
    
    /** 表格名称 */
    private String sheetName;
    
    
    /** 下一行 */
    private int nextRow;
    
    /** 下一列 */
    private int nextColumn;
    
    /** 最后一行行号*/
    @SuppressWarnings("unused")
    private int lastRowNumber;
    
    /** 最后一列列号  */
    @SuppressWarnings("unused")
    private int lastColumnNumber;
    
    /** 是否输出下一条文本记录 */
    private boolean outputNextStringRecord;
    
    /** 工作薄对象 */
    private HSSFWorkbook stubWorkbook;
    
    /** Sheet表格集合 */
    private BoundSheetRecord[] orderedBSRs;

    /** Sheet表格集合 */
    private List<BoundSheetRecord> boundSheetRecords;
    
    /** 打印输出对象 */
    @SuppressWarnings("unused")
    private PrintStream output;


    
    /**
     * 
     * 功能描述：重载构造函数。
     *
     */
    public HSSFParser(DataRowHandler dataRowHanlder){
        this.dataRowHanlder = dataRowHanlder;
        this.rowList = new ArrayList<String>();
        this.output = System.out;
        boundSheetRecords = new ArrayList<BoundSheetRecord>();
    }
    
    /* 
     * @see org.apache.poi.hssf.eventusermodel.HSSFListener#processRecord(org.apache.poi.hssf.record.Record)
     */
    @Override
    public void processRecord(Record record) {
        int thisRow = -1;
        int thisColumn = -1;
        String thisStr = null;
        String value = null;
        int index = this.rowList.size();

        switch (record.getSid()) {
            case BoundSheetRecord.sid: // 文档sheet记录
                boundSheetRecords.add((BoundSheetRecord)record);
                break;
            case BOFRecord.sid: // 单个sheet工作表
                BOFRecord br = (BOFRecord) record;
                if (br.getType() == BOFRecord.TYPE_WORKSHEET) {
                    // Create sub workbook if required
                    if (workbookBuildingListener != null && stubWorkbook == null) {
                        stubWorkbook = workbookBuildingListener.getStubHSSFWorkbook();
                    }

                    // Works by ordering the BSRs by the location of
                    // their BOFRecords, and then knowing that we
                    // process BOFRecords in byte offset order
                    sheetIndex++;
                    if (orderedBSRs == null) {
                        orderedBSRs = BoundSheetRecord.orderByBofPosition(boundSheetRecords);
                    }
                    sheetName = orderedBSRs[sheetIndex].getSheetname();
                    //System.out.println("第" + (this.sheetIndex + 1) + "表格：" + sheetName);
                }
                break;

            case SSTRecord.sid: // 待处理的记录
                sstRecord = (SSTRecord) record;
                break;

            case BlankRecord.sid: // 空白记录
                BlankRecord brec = (BlankRecord) record;

                thisRow = brec.getRow();
                thisColumn = brec.getColumn();
                if(blankCell){
                    rowList.add(index, " ");
                }
                break;
            case BoolErrRecord.sid: // 逻辑错误记录
                BoolErrRecord berec = (BoolErrRecord) record;

                thisRow = berec.getRow();
                thisColumn = berec.getColumn();
                thisStr = "";
                break;

            case FormulaRecord.sid: // 公式记录
                FormulaRecord frec = (FormulaRecord) record;

                thisRow = frec.getRow();
                thisColumn = frec.getColumn();

                if (outputFormulaValues) {
                    if (Double.isNaN(frec.getValue())) {
                        // Formula result is a string
                        // This is stored in the next record
                        outputNextStringRecord = true;
                        nextRow = frec.getRow();
                        nextColumn = frec.getColumn();
                    } else {
                        thisStr = formatListener.formatNumberDateCell(frec);
                    }
                } else {
                    thisStr = '"' + HSSFFormulaParser.toFormulaString(stubWorkbook, frec.getParsedExpression()) + '"';
                }
                break;
            case StringRecord.sid: // 文本记录
                if (outputNextStringRecord) {
                    // String for formula
                    StringRecord srec = (StringRecord) record;
                    thisStr = srec.getString();
                    thisRow = nextRow;
                    thisColumn = nextColumn;
                    outputNextStringRecord = false;
                }
                break;

            case LabelRecord.sid: // 标签记录
                LabelRecord lrec = (LabelRecord) record;

                currentRow = thisRow = lrec.getRow();
                thisColumn = lrec.getColumn();
                value = lrec.getValue().trim();
                value = value.equals("") ? " " : value;
                this.rowList.add(index, value);
                break;
            case LabelSSTRecord.sid:   // 标签处理记录
                LabelSSTRecord lsrec = (LabelSSTRecord) record;

                currentRow = thisRow = lsrec.getRow();
                thisColumn = lsrec.getColumn();
                if (sstRecord == null) {
                    rowList.add(index, " ");
                } else {
                    value = sstRecord.getString(lsrec.getSSTIndex()).toString().trim();
                    value = value.equals("") ? " " : value;
                    rowList.add(index, value);
                }
                break;
            case NoteRecord.sid:// 备注记录
                NoteRecord nrec = (NoteRecord) record;

                thisRow = nrec.getRow();
                thisColumn = nrec.getColumn();
                // TODO: Find object to match nrec.getShapeId()
                thisStr = '"' + "(TODO)" + '"';
                break;
            case NumberRecord.sid:  // 数值记录
                NumberRecord numrec = (NumberRecord) record;

                currentRow = thisRow = numrec.getRow();
                thisColumn = numrec.getColumn();
                int formatIndex = formatListener.getFormatIndex(numrec);
                String formatString = formatListener.getFormatString(numrec);
                if (DATE_FORMATE_STRING_US.equals(formatString))
                {
                    value = formatter.formatRawCellContents(numrec.getValue(), formatIndex, dateFormateString);
                }else{
                    value = Double.toString(numrec.getValue());
                }
                
                // Format
                rowList.add(index, value);
                break;
            case RKRecord.sid: // 主键记录
                RKRecord rkrec = (RKRecord) record;

                thisRow = rkrec.getRow();
                thisColumn = rkrec.getColumn();
                thisStr = '"' + "(TODO)" + '"';
                break;
            default:
                break;
        }

        // 空值的操作
        if (record instanceof MissingCellDummyRecord) {
            MissingCellDummyRecord mc = (MissingCellDummyRecord) record;
            currentRow = thisRow = mc.getRow();
            thisColumn = mc.getColumn();
            rowList.add(index, " ");
        }

        // 打印输出
        if (thisStr != null) {
//            if (thisColumn > 0) {
//                output.print(',');
//            }
//            output.print(thisStr);
        }

        // 更新行和列的值
        if (thisRow > -1)
            lastRowNumber = thisRow;
        if (thisColumn > -1)
            lastColumnNumber = thisColumn;

        // 行结束时的操作
        if (record instanceof LastCellOfRowDummyRecord) {
            // 行结束时， 调用 optRows() 方法
            this.dataRowHanlder.handlerRow(sheetIndex, sheetName, currentRow, rowList);

            lastColumnNumber = -1;
            rowList.clear();
        }
    }

    /* 
     * @see com.platform.common.excel.parser.ExcelDataParser#parser(java.lang.String)
     */
    @Override
    public void parser(String fileName) throws Throwable {
        HSSFEventFactory factory = new HSSFEventFactory();
        HSSFRequest request = new HSSFRequest();

        // 建立监听器
        MissingRecordAwareHSSFListener listener = new MissingRecordAwareHSSFListener(this);
        formatListener = new FormatTrackingHSSFListener(listener);
        
        if (outputFormulaValues) {
            request.addListenerForAllRecords(formatListener);
        } else {
            workbookBuildingListener = new SheetRecordCollectingListener(formatListener);
            request.addListenerForAllRecords(workbookBuildingListener);
        }
        
        // 解析文件并处理监听请求
        POIFSFileSystem poiFile = new POIFSFileSystem(new FileInputStream(fileName));
        factory.processWorkbookEvents(request, poiFile);
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
}
