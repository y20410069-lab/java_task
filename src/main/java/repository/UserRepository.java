package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import model.User;

/**
 * usersテーブルへのデータアクセスを担当するリポジトリクラスです。
 */
public class UserRepository extends BaseRepository {

    /**
     * すべてのユーザー情報を取得します。
     */
    public List<User> findAll() {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM users ORDER BY user_id";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                userList.add(mapResultSetToUser(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "findAll");
        }

        return userList;
    }

    /**
     * 指定されたIDのユーザー情報を1件取得します。
     */
    public User findById(int id) {
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE user_id = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "findById");
        }

        return null;
    }

    /**
     * ユーザー名による単件検索（認証用）
     */
    public User findByUsername(String username) {
        String sql = "SELECT user_id, username, password, email, created_at, updated_at FROM users WHERE username = ?";

        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "findByUsername");
        }

        return null;
    }

    /**
     * ResultSetからUserオブジェクトへのマッピング処理
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getInt("user_id")); // 修正: setUserId から setId に変更
        user.setUsername(rs.getString("username"));
        user.setPassword(rs.getString("password"));
        user.setEmail(rs.getString("email"));
        user.setCreatedAt(rs.getTimestamp("created_at"));
        user.setUpdatedAt(rs.getTimestamp("updated_at"));
        return user;
    }
}