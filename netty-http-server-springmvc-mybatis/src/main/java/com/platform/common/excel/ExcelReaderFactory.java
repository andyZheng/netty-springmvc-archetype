/*
 * 文件名：ExcelEventReader.java
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
import java.util.Map;

import com.platform.common.excel.entity.Sheet;
import com.platform.common.excel.listeners.DataRowListener;

/**
 * 功能描述： <code>ExcelReaderFactory</code>为Excel读取器实例工厂类。
 * <P>
 * 功能详细描述：本类提供单实例的Excel读取器。
 * 
 * @author andy.zheng0807@gmail.com
 * @version 1.0, 2013-7-29 下午4:55:08
 * @since Commons Platform 1.0
 */
public final class ExcelReaderFactory {

    /**
     * 
     * 功能描述：构造函数私有化。
     * 
     */
    private ExcelReaderFactory() {

    }

    /**
     * 
     * 功能描述：获取Excel文件简单读取器。
     * 
     * @return 返回获取到的文件简单读取器对象。
     */
    public static SimpleExcelReader getSimpleReaderInstance() {
        return new SimpleExcelReader();
    }

    /**
     * 
     * 功能描述：获取Excel文件读取器。
     * 
     * @return 返回获取到的文件读取器对象。
     */
    public static BigDataExcelReader getBigDataReaderInstance() {
        return new BigDataExcelReader();
    }

    public static void main(String[] args) {

        try {
            // TODO testing Excel文件简单读取器
            System.out.println("===================== Excel文件简单读取器  =============================");
            SimpleExcelReader simpleReader = (SimpleExcelReader) ExcelReaderFactory.getSimpleReaderInstance();
            simpleReader.setDateFormateString("yyyy-MM-dd");
            simpleReader.setDataRowListener(new DataRowListener() {

                @Override
                public void parseDataRow(int sheetIndex, String sheetName, int currentRow, List<String> rowList) {
                    System.out.println(rowList);
                }
            });
            simpleReader.load("C:\\employees_salary\\2014-3-11.xlsx");
            System.out.println("表格总数：" + simpleReader.getSheetCount());
            System.out.println("=====================================================================================");

            // TODO testing Excel文件大数据读取器
            System.out.println("===================== Excel文件大数据读取器  =============================");
            BigDataExcelReader bigDataReader = (BigDataExcelReader) ExcelReaderFactory.getBigDataReaderInstance();
            bigDataReader.setDateFormateString("yyyy-MM-dd");
            bigDataReader.setDataSeparator("#");
            bigDataReader.load("C:\\employees_salary\\2014-3-11.xlsx");

            System.out.println("表格总数：" + bigDataReader.getSheetCount());

            System.out.println();
            Map<String, Sheet> sheets = bigDataReader.getMoreSheetDataList();
            for (String sheetIndex : sheets.keySet()) {
                System.out.println("第" + (Integer.valueOf(sheetIndex) + 1) + "个表格数据：");
                Sheet currentSheet = sheets.get(sheetIndex);
                System.out.println(currentSheet);
                System.out.println();
            }
            System.out.println("=====================================================================================");
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }
}
