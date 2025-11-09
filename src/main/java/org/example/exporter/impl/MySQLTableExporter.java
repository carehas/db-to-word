package org.example.exporter.impl;

import org.example.config.ExportConfig;
import org.example.dto.ColumnInfo;
import org.example.exporter.DatabaseTableExporter;
import org.example.utils.DatabaseConnection;
import org.example.utils.TableExportCommon;


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
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DatabaseConnection.getConnection(host, port, username, password, databaseName, databaseType);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取数据库中所有的表名
     * @return
     */
    @Override
    public List<String> getAllTableNames() throws SQLException {
        return TableExportCommon.getAllTableNames(connection);
    }

    /**
     * 获取指定表的所有字段名
     * @param tableName
     * @return
     */
    public List<ColumnInfo> getTableStructure(String tableName) throws SQLException {
        return TableExportCommon.getTableStructure(connection, tableName);
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
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
