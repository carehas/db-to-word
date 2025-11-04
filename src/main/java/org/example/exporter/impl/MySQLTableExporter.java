package org.example.exporter.impl;

import org.example.dto.ColumnInfo;
import org.example.exporter.Exporter;
import org.example.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.exporter
 * @CreateTime: 2025-11-03  13:55
 * @Description: MySQL数据库表格导出功能
 * @Version: 1.0
 */
public class MySQLTableExporter implements Exporter {
    public String url;
    public String username;
    public String password;

    private Connection connection = new DatabaseConnection(url, username, password)
            .getConnection();

    private String databaseName;

    public MySQLTableExporter() throws Exception {
    }

    /**
     * 获取数据库中所有的表名
     * @return
     */
    public List<String> getAllTableNames() throws SQLException {
        List<String> tableNames = new ArrayList<>();
        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet tables = metaData.getTables(databaseName, null, "%", new String[]{"TABLE"});
        while (tables.next()){
            String tableName = tables.getString("TABLE_NAME");
            tableNames.add(tableName);
        }

        return tableNames;
    }

    /**
     * 获取指定表的所有字段名
     * @param tableName
     * @return
     */
    public List<ColumnInfo> getTableStructure(String tableName) throws SQLException {
        List<ColumnInfo> columnInfos = new ArrayList<>();

        DatabaseMetaData metaData = connection.getMetaData();

        ResultSet columns = metaData.getColumns(null, null, tableName, "%");
        while (columns.next()){
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setColumnName(columns.getString("COLUMN_NAME"));
            columnInfo.setColumnType(columns.getString("TYPE_NAME"));
            columnInfo.setSize(columns.getInt("COLUMN_SIZE"));
            columnInfo.setNullable(columns.getString("NULLABLE").equals("YES"));
            columnInfo.setDefaultValue(columns.getString("COLUMN_DEF"));
            columnInfo.setRemarks(columns.getString("REMARKS"));
            // 判断是否为主键
            if (isPrimaryKey(metaData, tableName, columnInfo.getColumnName())) {
                columnInfo.setPrimaryKey(true);
            }

            columnInfos.add(columnInfo);
        }

        return columnInfos;
    }
    /***
     * @description:判断是否为主键
     */
    private boolean isPrimaryKey(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
        ResultSet primaryKeys = metaData.getPrimaryKeys(null, null, tableName);

        while (primaryKeys.next()){

            if(columnName.equals(primaryKeys.getString("COLUMN_NAME"))){
                return true;
            }
        }

        return false;
    }
    /***
     * @description: 关闭连接数据库
     */
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
