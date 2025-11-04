package org.example.exporter;

import org.example.dto.ColumnInfo;

import java.sql.SQLException;
import java.util.List;

public interface Exporter {
    List<ColumnInfo> getTableStructure(String tableName) throws SQLException;
    List<String> getAllTableNames() throws SQLException;
}
