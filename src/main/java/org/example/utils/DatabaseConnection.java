package org.example.utils;


import java.sql.Connection;
import java.sql.DriverManager;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example.utils
 * @CreateTime: 2025-11-03  13:57
 * @Description: 连接数据库
 * @Version: 1.0
 */
public class DatabaseConnection {


    private String url;

    private String username;

    private String password;

    public DatabaseConnection(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    //连接数据库
    public Connection getConnection() throws Exception {
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(url, username, password);
    }

}
