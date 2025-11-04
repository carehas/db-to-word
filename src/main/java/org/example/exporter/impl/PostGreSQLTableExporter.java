package org.example.exporter.impl;

import org.example.dto.ColumnInfo;
import org.example.exporter.Exporter;

import java.sql.SQLException;
import java.util.List;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.exporter.impl
 * @CreateTime: 2025-11-03  17:02
 * @Description: TODO
 * @Version: 1.0
 */
public class PostGreSQLTableExporter implements Exporter {
    /**
     * 获取数据库中指定表的结构信息
     * @param tableName
     * @return
     */
    @Override
    public List<ColumnInfo> getTableStructure(String tableName) throws SQLException {
        return null;
    }

    /**
     * 获取数据库中所有表名
     * @return
     */
    @Override
    public List<String> getAllTableNames() {
        return null;
    }
}
