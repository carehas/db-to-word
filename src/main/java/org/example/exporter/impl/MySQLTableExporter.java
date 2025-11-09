package org.example.exporter.impl;

import org.example.config.ExportConfig;
import org.example.dto.ColumnInfo;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.exporter
 * @CreateTime: 2025-11-03  13:55
 * @Description: MySQL数据库表格导出功能
 * @Version: 1.0
 */
@ExportConfig(databaseType = "mysql")
public class MySQLTableExporter extends DatabaseTableExporter {

    //这里可以使用单例模式
    private Connection connection;

    private String databaseName;

    public MySQLTableExporter() throws Exception {
    }

    @Override
    public void getConnection(String host, Integer port, String username,
                              String password, String databaseName, String databaseType){
        //为后续需要使用的变量赋值
        this.databaseName = databaseName;
        // 构建 JDBC URL 数据库的名字填到了 jdbcUrl 中
        String jdbcUrl = String.format("jdbc:%s://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
                databaseType, host, port, databaseName);
        System.out.println("JDBC URL: " + jdbcUrl);
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcUrl, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库中所有的表名
     * @return
     */
    @Override
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
    public boolean isPrimaryKey(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
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
    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
}
