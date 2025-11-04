package org.example;

import java.io.Console;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.Callable;

import org.example.exporter.impl.DatabaseTableExporter;
import org.example.utils.WordDocumentGenerator;
import picocli.CommandLine;
import picocli.CommandLine.*;

/**
 * @BelongsProject: db-to-word
 * @BelongsPackage: org.example
 * @CreateTime: 2025-11-03  21:49
 * @Description: 命令行参数解析以及程序入口
 * @Version: 1.0
 */
@Command(
        name = "db-export", // 命令名称
        mixinStandardHelpOptions = true, // 自动包含 --help 和 --version
        version = "db-export 1.0", // 版本信息
        description = "将数据库表结构导出为Word文档。"
        // 使用示例
        /*usageHelpWidth = 100, // 帮助信息的宽度
        descriptionHeading = "%n@|bold,underline 使用说明|@%n%n",
        parameterListHeading = "%n@|bold 参数:|@%n",
        optionListHeading = "%n@|bold 选项:|@%n",
        commandListHeading = "%n@|bold 子命令:|@%n",
        synopsisHeading = "%n"*/
)
public class DatabaseExportCommand implements Callable<Integer> {

    // ------------------- 命令行选项 (Options) -------------------
    @Option(names = {"-h", "--host"}, description = "数据库主机地址", defaultValue = "localhost")
    private String host;

    @Option(names = {"-P", "--port"}, description = "数据库端口", defaultValue = "3306")
    private int port;

    @Option(names = {"-d", "--database"}, description = "数据库名称 (必需)", required = true)
    private String database;

    @Option(names = {"-u", "--username"}, description = "数据库用户名 (必需)", required = true)
    private String username;

    @Option(names = {"-p", "--password"}, description = "数据库密码", required = true)
    private String password;

    @Option(names = {"--driver"}, description = "JDBC驱动类名", defaultValue = "com.mysql.cj.jdbc.Driver")
    private String driver;

    @Option(names = {"--url"}, description = "完整的JDBC URL (如果提供，将覆盖 host, port, database)")
    private String jdbcUrl;

    //@Option(names = {"-o", "--output"}, description = "输出的Word文件路径", defaultValue = "database_structure.docx")
    private String outputPath = "output/database_structure.docx";

    /*@Option(names = {"--config"}, description = "配置文件路径 (properties文件)，配置项会覆盖命令行参数")
    private java.nio.file.Path configFile;*/


    @Override
    public Integer call() throws Exception {
        // 1. picocli 已经自动将命令行输入解析并赋值给了上面的成员变量
        //    例如: java -jar app.jar -d mydb -u admin -p secret -o mydoc.docx
        //         会使 database="mydb", username="admin", password="secret", outputPath="mydoc.docx"

        // 2. 在这里使用这些参数
        System.out.println("正在准备连接数据库...");
        System.out.println("主机: " + host);
        System.out.println("端口: " + port);
        System.out.println("数据库: " + database);
        System.out.println("用户名: " + username);
        // 注意：不要打印密码！
        System.out.println("输出文件: " + outputPath);

        // 构建 JDBC URL
        String jdbcUrl = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", host, port, database);
        System.out.println("JDBC URL: " + jdbcUrl);

        DatabaseTableExporter exporter = null;
        try {
            // 3. 使用参数创建 DatabaseTableExporter 实例
            exporter = new DatabaseTableExporter(jdbcUrl, username, password, driver);

            // 5. 创建 Word 生成器并导出
            System.out.println("正在生成Word文档...");
            //需要获取表的结构
            WordDocumentGenerator generator = new WordDocumentGenerator();
            generator.exportToWord(outputPath, exporter);

            System.out.println("完成！文档已保存至: " + outputPath);
            return 0; // 返回 0 表示成功

        } catch (Exception e) {
            System.err.println("发生错误: " + e.getMessage());
            e.printStackTrace(); // 开发阶段可以打印堆栈，生产环境可移除
            return 1; // 返回非0值表示失败
        } finally {
            // 6. 确保资源被释放
            if (exporter != null) {
                exporter.close();
            }
        }

    }

    // ------------------- 配置处理方法 -------------------
    /*private void processConfiguration() throws IOException {

        Properties props = new Properties();

        // 1. 优先级最低: 从配置文件加载 (如果指定了)
        if (configFile != null) {
            if (!java.nio.file.Files.exists(configFile)) {
                throw new IOException("配置文件不存在: " + configFile);
            }
            try (InputStream input = java.nio.file.Files.newInputStream(configFile)) {
                props.load(input);
            }
            // 从配置文件中读取并可能覆盖命令行参数
            if (props.containsKey("db.host")) host = props.getProperty("db.host");
            if (props.containsKey("db.port")) port = Integer.parseInt(props.getProperty("db.port"));
            if (props.containsKey("db.database")) database = props.getProperty("db.database");
            if (props.containsKey("db.username")) username = props.getProperty("db.username");
            if (props.containsKey("db.password")) password = props.getProperty("db.password");
            if (props.containsKey("db.driver")) driver = props.getProperty("db.driver");
            if (props.containsKey("db.url")) jdbcUrl = props.getProperty("db.url");
            if (props.containsKey("word.output.path")) outputPath = props.getProperty("word.output.path");
        }

        // 2. 如果用户没有通过命令行提供密码，且配置文件也没有，需要提示输入
        if (password == null) {
            Console console = System.console();
            if (console != null) {
                char[] pwdChars = console.readPassword("请输入数据库密码: ");
                password = new String(pwdChars);
            } else {
                throw new IOException("无法读取密码输入。请使用 --password 选项。");
            }
        }

        // 3. 构建最终的 JDBC URL
        if (jdbcUrl == null || jdbcUrl.trim().isEmpty()) {
            // 根据驱动类型构建默认URL
            if (driver.contains("mysql")) {
                jdbcUrl = String.format("jdbc:mysql://%s:%d/%s?useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC", host, port, database);
            } else if (driver.contains("postgresql")) {
                jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, database);
            } else if (driver.contains("oracle")) {
                jdbcUrl = String.format("jdbc:oracle:thin:@%s:%d:%s", host, port, database); // 注意Oracle SID格式可能不同
            } else {
                jdbcUrl = String.format("jdbc:%s://%s:%d/%s", driver.toLowerCase().replaceAll(".*\\.", ""), host, port, database);
            }
        }
        // 将最终确定的配置传递给 DatabaseTableExporter (可以通过系统属性或修改其构造函数)
        // 这里我们假设 DatabaseTableExporter 已经能通过系统属性或环境变量获取，或者我们修改它来接受参数
        // 为了简单，我们可以设置系统属性
        System.setProperty("db.url", jdbcUrl);
        System.setProperty("db.username", username);
        System.setProperty("db.password", password);
        System.setProperty("db.driver", driver);
    }*/

    public static void main(String[] args) {
        System.setProperty("picocli.color.enabled", "true");

        int exitCode = new CommandLine(new DatabaseExportCommand())
                .setCaseInsensitiveEnumValuesAllowed(true) // 枚举值不区分大小写
                .execute(args);
        System.exit(exitCode);
    }
}
