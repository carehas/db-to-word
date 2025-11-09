package org.example.utils;

import org.example.dto.ColumnInfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.common
 * @CreateTime: 2025-11-09  15:36
 * @Description: 表格导出功能抽象类
 * @Version: 1.0
 */

public class TableExportCommon {

    //获取所有表的名称
    public static List<String> getAllTableNames(Connection connection) throws SQLException {
        List<String> tableNames = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet tables = metaData.getTables(connection.getCatalog(), null, "%", new String[]{"TABLE"});
        while (tables.next()){
            String tableName = tables.getString("TABLE_NAME");
            tableNames.add(tableName);
        }
        return tableNames;
    }
    // 模板方法：获取表结构的通用流程
    public static List<ColumnInfo> getTableStructure(Connection connection, String tableName) throws SQLException {
        List<ColumnInfo> columnInfos = new ArrayList<>();

        ResultSet columns = connection.getMetaData().getColumns(null, null, tableName, "%");
        while (columns.next()){
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(columns.getString("COLUMN_NAME"));
            columnInfo.setColumnType(columns.getString("TYPE_NAME"));
            columnInfo.setSize(columns.getInt("COLUMN_SIZE"));
            columnInfo.setNullable(columns.getString("NULLABLE").equals("YES"));
            columnInfo.setDefaultValue(columns.getString("COLUMN_DEF"));
            columnInfo.setRemarks(columns.getString("REMARKS"));
            // 判断是否为主键
            if (isPrimaryKey(connection.getMetaData(), tableName, columnInfo.getColumnName())) {
                columnInfo.setPrimaryKey(true);
            }

            columnInfos.add(columnInfo);
        }

        return columnInfos;
    }

    public static boolean isPrimaryKey(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);
        while (primaryKeys.next()){

            if(columnName.equals(primaryKeys.getString("COLUMN_NAME"))){
                return true;
            }
        }
        return false;
    }
}
