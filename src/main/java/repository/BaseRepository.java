package repository;

import java.sql.Connection;
import java.sql.SQLException;

import database.ConnectionFactory;

/**
 * すべてのリポジトリクラスの基盤となる抽象クラスです。
 * データベースへの接続取得機能を共通化しています。
 */
public abstract class BaseRepository {
    
    /**
     * データベース接続（Connection）を取得します。
     * @return 接続オブジェクト
     * @throws SQLException 接続失敗時に発生
     */
    protected Connection getConnection() throws SQLException {
        return ConnectionFactory.getConnection();
    }

    /**
     * SQL実行時の例外処理を共通化してログに出力します。
     * @param e 発生したSQLException
     * @param operation 実行中だったメソッド名
     */
    protected void handleSQLException(SQLException e, String operation) {
        System.err.println("【DBエラー】 " + operation + " の実行中にエラーが発生しました。");
        System.err.println("エラー詳細: " + e.getMessage());
        e.printStackTrace();
    }
}