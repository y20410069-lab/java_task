package action;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import repository.TaskRepository;

/**
 * タスクの削除処理を行うアクションクラスです。
 */
public class TaskDeleteAction extends BaseAuthAction {

    private final TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws Exception {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            return "redirect:/app/task/list";
        }

        try {
            int taskId = Integer.parseInt(idStr);
            // 削除実行（内部で user_id も条件に含まれるため安全）
            taskRepository.deleteByIdAndUserId(taskId, loginUser.getId());
        } catch (NumberFormatException e) {
            // 無効なIDは無視して一覧へ
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return "redirect:/app/task/list";
    }
}