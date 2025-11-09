# 数据库表结构导出工具 (db-to-word)

一个命令行工具，用于将数据库中的表结构导出为 Word 文档（.docx 格式）。

## 功能特性

- 支持多种数据库：MySQL、PostgreSQL、Oracle
- 通过命令行参数配置连接信息
- 自动生成包含表名、列名、数据类型、长度、是否可为空、主键和备注的表格
- 使用反射机制支持扩展更多数据库类型
- 生成的 Word 文档格式清晰，易于阅读

## 快速开始

### 环境要求

- Java 8 或更高版本
- Maven 3.6+ (用于构建项目)

### 构建项目

```bash
git clone <repository-url>
cd db-to-word
mvn clean package
```


构建完成后，可在 `target` 目录下找到可执行的 JAR 文件，例如 [db-to-word-1.0-SNAPSHOT.jar](file://D:\JavaCode\db-to-word\target\db-to-word-1.0-SNAPSHOT.jar)。

### 使用方法

```bash
java -jar target/db-to-word-1.0-SNAPSHOT.jar [选项]
```


#### 必需参数

- `-d`, `--database DATABASE`：数据库名称
- `-u`, `--username USERNAME`：数据库用户名
- `-p`, `--password PASSWORD`：数据库密码
- `-t`, `--databaseType DATABASETYPE`：数据库类型（支持 `mysql`, `postgresql`, `oracle`）

#### 可选参数

- `-h`, `--host HOST`：数据库主机地址（默认: localhost）
- `-P`, `--port PORT`：数据库端口（默认: 根据数据库类型自动选择，MySQL: 3306, PostgreSQL: 5432, Oracle: 1521）
- `-o`, `--output OUTPUT`：输出的 Word 文件路径（默认: output/database_structure.docx）

#### 示例

```bash
# 导出 MySQL 数据库结构
java -jar target/db-to-word-1.0-SNAPSHOT.jar -h localhost -P 3306 -d mydb -u root -p password -t mysql -o mydb_structure.docx

# 导出 PostgreSQL 数据库结构
java -jar target/db-to-word-1.0-SNAPSHOT.jar -h localhost -P 5432 -d mydb -u postgres -p password -t postgresql -o mydb_structure.docx
```


## 项目结构

```
src/main/java/org/example
├── DatabaseExportCommand.java      # 命令行入口和参数解析
├── config                          # 配置相关注解
│   └── ExportConfig.java
├── dto                             # 数据传输对象
│   └── ColumnInfo.java
├── exporter                        # 数据库导出器抽象和实现
│   ├── DatabaseTableExporter.java
│   └── impl
│       ├── MySQLTableExporter.java
│       ├── PostGreSQLTableExporter.java
│       └── OracleTableExporter.java
├── utils                           # 工具类
│   ├── DatabaseConnection.java
│   ├── ReflectConstruct.java
│   ├── TableExportCommon.java
│   └── WordDocumentGenerator.java
```


## 扩展支持新的数据库

1. 创建新的类继承 [DatabaseTableExporter](file://D:\JavaCode\db-to-word\src\main\java\org\example\exporter\DatabaseTableExporter.java#L16-L26) 抽象类
2. 实现 [getConnection](file://D:\JavaCode\db-to-word\src\main\java\org\example\utils\DatabaseConnection.java#L25-L36), [getAllTableNames](file://D:\JavaCode\db-to-word\src\main\java\org\example\exporter\DatabaseTableExporter.java#L20-L20), [getTableStructure](file://D:\JavaCode\db-to-word\src\main\java\org\example\exporter\DatabaseTableExporter.java#L22-L22), [close](file://D:\JavaCode\db-to-word\src\main\java\org\example\exporter\DatabaseTableExporter.java#L24-L24) 和 [getDatabaseName](file://D:\JavaCode\db-to-word\src\main\java\org\example\exporter\DatabaseTableExporter.java#L25-L25) 方法
3. 在类上添加 `@ExportConfig(databaseType = "your_database_type")` 注解
4. 在 [pom.xml](file://D:\JavaCode\db-to-word\pom.xml) 中添加对应的 JDBC 驱动依赖
5. 重新构建项目

## 依赖

- [Apache POI](https://poi.apache.org/)：用于生成 Word 文档
- [Picocli](https://picocli.info/)：用于命令行参数解析
- [Reflections](https://github.com/ronmamo/reflections)：用于运行时扫描类
- 数据库 JDBC 驱动：
  - MySQL Connector/J
  - PostgreSQL JDBC Driver
  - Oracle JDBC Driver
