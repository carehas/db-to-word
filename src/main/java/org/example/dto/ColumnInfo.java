package org.example.dto;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.dto
 * @CreateTime: 2025-11-03  14:18
 * @Description: 列表信息
 * @Version: 1.0
 */
public class ColumnInfo {
    private String columnName;

    private String columnType;

    private Integer size;

    private Boolean isNullable;

    private Boolean isPrimaryKey;

    private String defaultValue;
    private String remarks;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnType() {
        return columnType;
    }

    public void setColumnType(String columnType) {
        this.columnType = columnType;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public Boolean getNullable() {
        return isNullable;
    }

    public void setNullable(Boolean nullable) {
        isNullable = nullable;
    }

    public Boolean getPrimaryKey() {
        return isPrimaryKey;
    }

    public void setPrimaryKey(Boolean primaryKey) {
        isPrimaryKey = primaryKey;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    @Override
    public String toString() {
        return String.format("Column{name='%s', type='%s', size=%d, nullable=%s, primaryKey=%s, remarks='%s'}",
                columnName, columnType, size, isNullable, isPrimaryKey, remarks);
    }
}
