package org.example.exporter.impl;

import org.example.config.ExportConfig;
import org.example.dto.ColumnInfo;
import org.example.exporter.DatabaseTableExporter;
import org.example.utils.DatabaseConnection;
import org.example.utils.TableExportCommon;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.exporter.impl
 * @CreateTime: 2025-11-09  16:26
 * @Description: Oracle数据库表格导出功能
 * @Version: 1.0
 */
@ExportConfig(databaseType = "oracle")
public class OracleTableExporter extends DatabaseTableExporter {
    private String databaseName;
    private Connection connection;
    @Override
    public void getConnection(String host, Integer port, String username, String password, String database, String databaseType) {
        this.databaseName = database;
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
            connection = DatabaseConnection.getConnection(host, port, username, password, database, databaseType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<String> getAllTableNames() throws SQLException {
        return TableExportCommon.getAllTableNames(connection);
    }

    @Override
    public List<ColumnInfo> getTableStructure(String tableName) throws SQLException {
        return TableExportCommon.getTableStructure(connection, tableName);
    }

    @Override
    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()){
            connection.close();
        }
    }

    @Override
    public String getDatabaseName() {
        return databaseName;
    }
}
