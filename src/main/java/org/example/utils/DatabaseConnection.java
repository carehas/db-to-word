package org.example.utils;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.utils
 * @CreateTime: 2025-11-03  13:57
 * @Description: 连接数据库
 * @Version: 1.0
 */
public class DatabaseConnection {

    private String host;
    private Integer port;
    private String username;
    private String password;
    private String databaseName;
    private String databaseType;

    private static String url;
    //连接数据库
    public static Connection getConnection(String host, Integer port, String username,
                                    String password, String databaseName, String databaseType) throws Exception {
        // 构建 JDBC URL 数据库的名字填到了 jdbcUrl 中
        url = java.lang.String.format("jdbc:%s://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC",
                databaseType, host, port, databaseName);
        System.out.println("JDBC URL: " + url);
        try {
            return DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

}
