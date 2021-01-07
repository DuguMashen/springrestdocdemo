package com.dugumashen.springrestdocdemo.util;


import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @author
 */
public class PoiUtil {
    public static SXSSFWorkbook generateSXSSFWorkbook(String sheetName, LinkedHashMap<String, String> title, List<Map<String, String>> data, SXSSFWorkbook workbook) {
        if (title == null || title.isEmpty()) {
            throw new RuntimeException("标题列为空");
        }
        if (workbook == null) {
            workbook = new SXSSFWorkbook(1000);
        }

        //创建sheet
        Sheet sheet = workbook.createSheet(sheetName);
        //设置第0行
        Row row = sheet.createRow(0);
        //设置样式
        CellStyle style = workbook.createCellStyle();
        style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        //列对象
        Cell cell = null;
        Iterator<Map.Entry<String, String>> entryIterator = title.entrySet().iterator();

        //使用ArrayList保证title与data顺序一致
        List<String> keyArray = new ArrayList<>(title.size());
        while (entryIterator.hasNext()) {
            Map.Entry<String, String> el = entryIterator.next();
            keyArray.add(el.getKey());
        }

        //顺序设置title
        for (int col = 0; col < keyArray.size(); col++) {
            cell = row.createCell(col);
            cell.setCellValue(title.get(keyArray.get(col)));
            cell.setCellStyle(style);
        }
        for (int i = 0; i < data.size(); i++) {
            row = sheet.createRow(i + 1);
            Map<String, String> map = data.get(i);
            //按照取出title的顺序取出data
            for (int j = 0; j < keyArray.size(); j++) {

                cell = row.createCell(j);
                cell.setCellStyle(style);
                cell.setCellValue(map.get(keyArray.get(j)));
            }
        }
        return workbook;
    }


    public static void exportExcel(OutputStream out, SXSSFWorkbook workbook) {
        try {
            workbook.write(out);
            out.flush();
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
