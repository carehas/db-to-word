package org.example.exporter.impl;

import org.example.config.ExportConfig;
import org.example.dto.ColumnInfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.exporter.impl
 * @CreateTime: 2025-11-03  17:02
 * @Description: PostGreSQL数据库表结构导出实现类
 * @Version: 1.0
 */
@ExportConfig(databaseType = "postgresql")
public class PostGreSQLTableExporter extends DatabaseTableExporter {

    private String databaseName;
    private Connection connection;
    private String url;
    public PostGreSQLTableExporter() throws Exception {
    }

    @Override
    public void getConnection(String host, Integer port, String username,
                              String password, String databaseName, String databaseType) {
        this.databaseName = databaseName;
        url = String.format("jdbc:%s://%s:%d/%s?currentSchema=public&ssl=false",
                databaseType, host, port, databaseName);
        System.out.println("JDBC URL: " + url);
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllTableNames() throws SQLException {

        return null;
    }

    @Override
    public List<ColumnInfo> getTableStructure(String tableName) throws SQLException {
        return null;
    }

    @Override
    public boolean isPrimaryKey(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException {
        return false;
    }
    /***
     * @description: 关闭数据库
     */
    @Override
    public void close() throws SQLException {

    }
}
