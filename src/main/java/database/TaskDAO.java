package database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {

    public List<Task> findAll() {
        List<Task> taskList = new ArrayList<>();
        String sql = "SELECT * FROM tasks";

        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Task task = new Task();
                task.setTaskId(rs.getInt("task_id"));
                task.setUserId(rs.getInt("user_id"));
                task.setTitle(rs.getString("title"));
                task.setDescription(rs.getString("description"));
                task.setStatus(rs.getString("status"));
                task.setPriority(rs.getString("priority"));
                task.setCreatedAt(rs.getTimestamp("created_at"));
                task.setUpdatedAt(rs.getTimestamp("updated_at"));

                taskList.add(task);
            }

        } catch (SQLException e) {
            System.err.println("TaskDAO.findAll でエラーが発生しました: " + e.getMessage());
            e.printStackTrace();
        }

        return taskList;
    }
}