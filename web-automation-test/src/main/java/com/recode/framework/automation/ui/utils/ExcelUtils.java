package com.recode.framework.automation.ui.utils;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;

public class ExcelUtils {

    // Method - Get test data from the excel file using test id and excel column name.
    public static String getTestData(String fileName, String sheetName, String testCaseID, String columnName) throws IOException
    {
        Workbook xlWorkBook = null;
        int rowIndex = 0, cellIndex = 0;
        boolean rowIndexCheck = false, cellIndexCheck = false;

        ClassLoader classLoader = ExcelUtils.class.getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream(fileName);
        DataFormatter formatter = new DataFormatter();

        //Find the file extension by splitting file name in substring  and getting only extension name
        String fileExtensionName = fileName.substring(fileName.indexOf("."));
        //Check condition if the file is xlsx or xls file
        if(fileExtensionName.equals(".xlsx")){
            assert inputStream != null;
            xlWorkBook = new XSSFWorkbook(inputStream);
        }
        else if(fileExtensionName.equals(".xls")){
            assert inputStream != null;
            xlWorkBook = new HSSFWorkbook(inputStream);
        }

        assert xlWorkBook != null;
        Sheet xlWorkSheet = xlWorkBook.getSheet(sheetName);

        int totalRowCount = xlWorkSheet.getLastRowNum();
        assert totalRowCount != 0;
        Row firstRow = xlWorkSheet.getRow(0);
        for (int cCnt=0; cCnt<firstRow.getLastCellNum(); cCnt++){
            // Condition to get the specific column index from the excel file.
            if(formatter.formatCellValue(firstRow.getCell(cCnt)).equals(columnName)){
                cellIndexCheck = true;
                cellIndex = cCnt;
                break;
            }
        }
        for (int rCnt = 0; rCnt < totalRowCount+1; rCnt++) {
            Row row = xlWorkSheet.getRow(rCnt);
            // Condition to get the specific test id row index from the excel file.
            if (formatter.formatCellValue(row.getCell(0)).equals(testCaseID)) {
                rowIndexCheck = true;
                rowIndex = rCnt;
                break;
            }
        }
        // It returns value only if the loop find testcase id and column name from the excel file.
        if(rowIndexCheck && cellIndexCheck) {
            return xlWorkSheet.getRow(rowIndex).getCell(cellIndex).toString();
        }
        else {
            return null;
        }
    }

}
