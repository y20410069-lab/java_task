package repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.Task;

/**
 * tasksテーブルへのデータアクセスおよび所有者確認を担当するリポジトリクラスです。
 */
public class TaskRepository extends BaseRepository {

    /**
     * 指定されたタスクの所有者がログインユーザー自身であるかを確認します（認可制御用）。
     */
    public boolean isOwner(int taskId, int userId) {
        String sql = "SELECT COUNT(*) FROM tasks WHERE task_id = ? AND user_id = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, taskId);
            ps.setInt(2, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "isOwner");
        }
        return false;
    }

    // === 【新規追加】お気に入り状態切り替えメソッド ===
    public boolean toggleFavorite(int taskId, int userId) throws SQLException {
        String sql = "UPDATE tasks SET is_favorite = NOT is_favorite, updated_at = NOW() WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, userId);
            
            int updatedRows = pstmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e, "toggleFavorite");
            throw e;
        }
    }

    public List<Task> findAll() {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT task_id, user_id, title, description, status, priority, is_favorite, created_at, updated_at FROM tasks ORDER BY is_favorite DESC, created_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                taskList.add(mapResultSetToTask(rs));
            }
        } catch (SQLException e) {
            handleSQLException(e, "findAll");
        }
        return taskList;
    }

    public List<Task> findByUserId(int userId) {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT task_id, user_id, title, description, status, priority, is_favorite, created_at, updated_at FROM tasks WHERE user_id = ? ORDER BY is_favorite DESC, created_at DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    taskList.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "findByUserId");
        }
        return taskList;
    }

    public boolean save(Task task) throws SQLException {
        String sql = "INSERT INTO tasks (user_id, title, description, status, priority, is_favorite, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, NOW(), NOW())";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            pstmt.setInt(1, task.getUserId());
            pstmt.setString(2, task.getTitle());
            pstmt.setString(3, task.getDescription());
            pstmt.setString(4, task.getStatus());
            pstmt.setString(5, task.getPriority());
            pstmt.setBoolean(6, task.isFavorite()); // お気に入り反映
            
            int insertedRows = pstmt.executeUpdate();
            if (insertedRows > 0) {
                try (ResultSet rs = pstmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        task.setId(rs.getInt(1));
                    }
                }
                return true;
            }
        } catch (SQLException e) {
            handleSQLException(e, "save");
            throw e;
        }
        return false;
    }

    public boolean update(Task task) throws SQLException {
        String sql = "UPDATE tasks SET title = ?, description = ?, status = ?, priority = ?, is_favorite = ?, updated_at = NOW() WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, task.getTitle());
            pstmt.setString(2, task.getDescription());
            pstmt.setString(3, task.getStatus());
            pstmt.setString(4, task.getPriority());
            pstmt.setBoolean(5, task.isFavorite()); // お気に入り反映
            pstmt.setInt(6, task.getId());
            pstmt.setInt(7, task.getUserId());
            
            int updatedRows = pstmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e, "update");
            throw e;
        }
    }

    public boolean deleteByIdAndUserId(int taskId, int userId) throws SQLException {
        String sql = "DELETE FROM tasks WHERE task_id = ? AND user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, taskId);
            pstmt.setInt(2, userId);
            
            int deletedRows = pstmt.executeUpdate();
            return deletedRows > 0;
        } catch (SQLException e) {
            handleSQLException(e, "deleteByIdAndUserId");
            throw e;
        }
    }

    public List<Task> search(int userId, String keyword, String sort) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT task_id, user_id, title, description, status, priority, is_favorite, created_at, updated_at FROM tasks WHERE user_id = ?");
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        
        if (hasKeyword) {
            sql.append(" AND title LIKE ?");
        }
        if ("ASC".equals(sort)) {
            sql.append(" ORDER BY is_favorite DESC, created_at ASC");
        } else {
            sql.append(" ORDER BY is_favorite DESC, created_at DESC");
        }

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, userId);
            if (hasKeyword) {
                stmt.setString(paramIndex++, "%" + keyword.trim() + "%");
            }
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "search");
            throw e;
        }
        return tasks;
    }

    public List<Task> findAllByUserIdWithSort(int userId, String sort) throws SQLException {
        return search(userId, "", sort);
    }

    // === 総件数取得メソッド (ページング検索対応) ===
    public int countTasks(int userId, String keyword) throws SQLException {
        StringBuilder sql = new StringBuilder("SELECT COUNT(*) FROM tasks WHERE user_id = ?");
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        
        if (hasKeyword) {
            sql.append(" AND title LIKE ?");
        }
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, userId);
            if (hasKeyword) {
                stmt.setString(paramIndex++, "%" + keyword.trim() + "%");
            }
            
            try (ResultSet rs = pstmtOrStmtExecuteQuery(stmt)) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "countTasks");
            throw e;
        }
        return 0;
    }

    private ResultSet pstmtOrStmtExecuteQuery(PreparedStatement stmt) throws SQLException {
        return stmt.executeQuery();
    }

    // === ページング対応検索メソッド ===
    public List<Task> searchWithPaging(int userId, String keyword, String sort, int pageSize, int offset) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        StringBuilder sql = new StringBuilder("SELECT task_id, user_id, title, description, status, priority, is_favorite, created_at, updated_at FROM tasks WHERE user_id = ?");
        boolean hasKeyword = (keyword != null && !keyword.trim().isEmpty());
        
        if (hasKeyword) {
            sql.append(" AND title LIKE ?");
        }
        
        if ("ASC".equals(sort)) {
            sql.append(" ORDER BY is_favorite DESC, created_at ASC");
        } else {
            sql.append(" ORDER BY is_favorite DESC, created_at DESC");
        }
        
        sql.append(" LIMIT ? OFFSET ?");
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql.toString())) {
            
            int paramIndex = 1;
            stmt.setInt(paramIndex++, userId);
            if (hasKeyword) {
                stmt.setString(paramIndex++, "%" + keyword.trim() + "%");
            }
            stmt.setInt(paramIndex++, pageSize);
            stmt.setInt(paramIndex++, offset);
            
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    tasks.add(mapResultSetToTask(rs));
                }
            }
        } catch (SQLException e) {
            handleSQLException(e, "searchWithPaging");
            throw e;
        }
        return tasks;
    }

    private Task mapResultSetToTask(ResultSet rs) throws SQLException {
        Task task = new Task();
        task.setId(rs.getInt("task_id"));
        task.setUserId(rs.getInt("user_id"));
        task.setTitle(rs.getString("title"));
        task.setDescription(rs.getString("description"));
        task.setStatus(rs.getString("status"));
        task.setPriority(rs.getString("priority"));
        task.setCreatedAt(rs.getTimestamp("created_at"));
        task.setUpdatedAt(rs.getTimestamp("updated_at"));
        task.setFavorite(rs.getBoolean("is_favorite")); // お気に入りマッピング追加
        return task;
    }
}