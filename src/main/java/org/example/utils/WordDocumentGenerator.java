package org.example.utils;

import org.apache.poi.xwpf.usermodel.*;
import org.example.dto.ColumnInfo;
import org.example.exporter.Exporter;
import org.example.exporter.impl.DatabaseTableExporter;
import org.example.exporter.impl.MySQLTableExporter;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTShd;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STShd;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.utils
 * @CreateTime: 2025-11-03  14:32
 * @Description: 生成Word文档
 * @Version: 1.0
 */
public class WordDocumentGenerator {

    public WordDocumentGenerator() throws Exception {

    }
    /**
     * @description:获取表的结构，并且生成Word文档
     */
    public void exportToWord(String outputFilePath, DatabaseTableExporter exporter)
            throws SQLException, IOException {

        XWPFDocument document = new XWPFDocument();
        List<String> tableNames = exporter.getAllTableNames();

        try {
            for (String tableName : tableNames) {
                //添加表名作为标题
                XWPFParagraph titleParagraph = document.createParagraph();
                XWPFRun titleRun = titleParagraph.createRun();
                titleRun.setText("表名" + tableName);
                titleRun.setBold(true);
                titleRun.setFontSize(16);
                titleRun.addCarriageReturn();

                //获取表的结构
                List<ColumnInfo> columnInfos = exporter.getTableStructure(tableName);

                //创建表格
                XWPFTable table = document.createTable(columnInfos.size() + 1, 6);

                //设置表格样式
                table.setWidth("100%");

                //创建表头
                XWPFTableRow headerRow = table.getRow(0);
                setCellText(headerRow.getCell(0), "列名", true);
                setCellText(headerRow.getCell(1), "数据类型", true);
                setCellText(headerRow.getCell(2), "长度", true);
                setCellText(headerRow.getCell(3), "是否可为空", true);
                setCellText(headerRow.getCell(4), "主键", true);
                setCellText(headerRow.getCell(5), "备注", true);

                //填充数据
                for (int i = 0; i < columnInfos.size(); i++) {
                    ColumnInfo columnInfo = columnInfos.get(i);
                    XWPFTableRow row = table.getRow(i + 1);

                    setCellText(row.getCell(0), columnInfo.getColumnName(), false);
                    setCellText(row.getCell(1), columnInfo.getColumnType(), false);
                    setCellText(row.getCell(2), String.valueOf(columnInfo.getSize()), false);
                    setCellText(row.getCell(3), columnInfo.getNullable() ? "是" : "否", false);
                    setCellText(row.getCell(4), (columnInfo.getPrimaryKey() != null && columnInfo.getPrimaryKey()) ? "是" : "否", false);
                    setCellText(row.getCell(5), columnInfo.getRemarks() != null ? columnInfo.getRemarks() : "", false);
                }
                //添加一个空段作为分割
                document.createParagraph();
            }

            // 保存文档
            File outputFile = new File(outputFilePath);
            File parentDir = outputFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            try (FileOutputStream out = new FileOutputStream(outputFilePath)){
                document.write(out);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                System.out.println("文件不存在");
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("文件保存失败");
            }

        } finally {
            document.close();
        }
    }

    /***
     * @description: 设置单元格文本和样式
     */
    private void setCellText(XWPFTableCell cell, String text, Boolean isHeader){
        cell.setText(text);
        if(isHeader){
            CTTcPr tcPr = cell.getCTTc().getTcPr();
            if(tcPr == null)    tcPr = cell.getCTTc().addNewTcPr();
            CTShd ctShd = tcPr.addNewShd();
            ctShd.setFill("CCCCCC");
            ctShd.setColor("auto");
            ctShd.setVal(STShd.CLEAR);
        }
    }
}
