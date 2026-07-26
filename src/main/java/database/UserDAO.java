package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    /**
     * usersテーブルからすべてのユーザー情報を取得します
     */
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";

        // try-with-resources で接続とリソースを自動管理
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                // ResultSetからデータを取り出してUserオブジェクトにマッピング
                user.setUserId(rs.getInt("user_id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setEmail(rs.getString("email"));
                user.setCreatedAt(rs.getTimestamp("created_at"));
                user.setUpdatedAt(rs.getTimestamp("updated_at"));

                // リストに追加
                userList.add(user);
            }

        } catch (SQLException e) {
            System.err.println("UserDAO.findAll でエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }

        return userList;
    }
}