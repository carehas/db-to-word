package org.example.exporter.impl;

import org.example.dto.ColumnInfo;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.exporter.impl
 * @CreateTime: 2025-11-09  14:00
 * @Description: 数据库表导出功能的抽象类
 * @Version: 1.0
 */
public abstract class DatabaseTableExporter {

    public abstract void getConnection(String host, Integer port, String username, String password, String database, String databaseType);

    public abstract List<String> getAllTableNames() throws SQLException;

    public abstract List<ColumnInfo> getTableStructure(String tableName) throws SQLException;

    public abstract boolean isPrimaryKey(DatabaseMetaData metaData, String tableName, String columnName) throws SQLException;
    public abstract void close() throws SQLException;
}
