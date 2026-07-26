package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {
    // 接続情報を定数として管理（マニュアル指定のパラメータを含める）
    private static final String DB_URL = 
        "jdbc:mysql://localhost:3306/taskmanager" +
        "?characterEncoding=utf8" +
        "&useUnicode=true" +
        "&serverTimezone=Asia/Tokyo" +
        "&useSSL=false"; // XAMPP開発環境用
        
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // XAMPPのデフォルト（空文字）
    private static final String DB_DRIVER = "com.mysql.cj.jdbc.Driver";

    // クラス読み込み時に一度だけドライバーをロード
    static {
        try {
            Class.forName(DB_DRIVER);
            System.out.println("MySQL Driver loaded successfully.");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL Driver not found: " + e.getMessage());
        }
    }

    /**
     * データベースへの接続オブジェクトを返却します
     */
    public static Connection getConnection() throws SQLException {
        try {
            System.out.println("Attempting to connect to: " + DB_URL);
            Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("Database connection successful!");
            return conn;
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e;
        }
    }
}