package action;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Task;
import model.User;
import repository.TaskRepository;
import util.ValidationUtil;

/**
 * タスクの編集・更新処理を行うアクションクラスです。
 */
public class TaskEditAction extends BaseAuthAction {

    private final TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws Exception {
        String method = request.getMethod();

        if ("POST".equalsIgnoreCase(method)) {
            return processUpdate(request, loginUser);
        }

        return showEditForm(request, loginUser);
    }

    /**
     * 編集フォームを表示する (GET)
     */
    private String showEditForm(HttpServletRequest request, User loginUser) {
        String idStr = request.getParameter("id");
        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("error", "タスクIDが指定されていません。");
            return "redirect:/app/task/list";
        }

        try {
            int taskId = Integer.parseInt(idStr);
            Task task = getTaskIfOwner(taskId, loginUser.getId());
            if (task == null) {
                request.setAttribute("error", "指定されたタスクが存在しないか、アクセス権限がありません。");
                return "redirect:/app/task/list";
            }

            request.setAttribute("task", task);
            return "task/edit";
        } catch (NumberFormatException e) {
            request.setAttribute("error", "無効なタスクIDです。");
            return "redirect:/app/task/list";
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベースエラーが発生しました。");
            return "redirect:/app/task/list";
        }
    }

    /**
     * 更新処理を実行する (POST)
     */
    private String processUpdate(HttpServletRequest request, User loginUser) {
        String idStr = request.getParameter("id");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");

        if (idStr == null || idStr.trim().isEmpty()) {
            request.setAttribute("error", "タスクIDが指定されていません。");
            return "redirect:/app/task/list";
        }

        try {
            int taskId = Integer.parseInt(idStr);

            // 所有者確認
            Task existingTask = getTaskIfOwner(taskId, loginUser.getId());
            if (existingTask == null) {
                request.setAttribute("error", "権限がありません。");
                return "redirect:/app/task/list";
            }

            // ① バリデーション実行（画面からの入力を検証）
            Map<String, List<String>> fieldErrors = new HashMap<>();

            List<String> titleErrors = ValidationUtil.validateTitle(title);
            if (titleErrors != null && !titleErrors.isEmpty()) {
                fieldErrors.put("title", titleErrors);
            }

            List<String> descErrors = ValidationUtil.validateDescription(description);
            if (descErrors != null && !descErrors.isEmpty()) {
                fieldErrors.put("description", descErrors);
            }

            List<String> statusErrors = ValidationUtil.validateStatus(status);
            if (statusErrors != null && !statusErrors.isEmpty()) {
                fieldErrors.put("status", statusErrors);
            }

            List<String> priorityErrors = ValidationUtil.validatePriority(priority);
            if (priorityErrors != null && !priorityErrors.isEmpty()) {
                fieldErrors.put("priority", priorityErrors);
            }

            // ② バリデーションエラーがある場合は再表示
            if (!fieldErrors.isEmpty()) {
                existingTask.setTitle(title != null ? title.trim() : "");
                existingTask.setDescription(description != null ? description.trim() : "");
                existingTask.setStatus(status != null ? status.trim() : "未着手");
                existingTask.setPriority(priority != null ? priority.trim() : "中");
                
                request.setAttribute("task", existingTask);
                request.setAttribute("errors", fieldErrors);
                return "task/edit";
            }

            // ③ 画面からの入力を DB保存用の値（英語表記）へ変換
            String dbStatus = convertStatusToDbValue(status);
            String dbPriority = convertPriorityToDbValue(priority);

            // 更新パラメータを生成
            Task updateTask = new Task();
            updateTask.setId(taskId);
            updateTask.setUserId(loginUser.getId());
            updateTask.setTitle(title != null ? title.trim() : "");
            updateTask.setDescription(description != null ? description.trim() : "");
            updateTask.setStatus(dbStatus);      // DB用値 (pending / in_progress / completed)
            updateTask.setPriority(dbPriority);  // DB用値 (low / medium / high)
            updateTask.setFavorite(existingTask.isFavorite());

            // ④ DB更新を実行
            boolean success = taskRepository.update(updateTask);
            if (success) {
                return "redirect:/app/task/list";
            } else {
                request.setAttribute("error", "タスクの更新に失敗しました。");
                request.setAttribute("task", updateTask);
                return "task/edit";
            }

        } catch (NumberFormatException e) {
            request.setAttribute("error", "無効なタスクIDです。");
            return "redirect:/app/task/list";
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベースエラーが発生しました。");
            return "redirect:/app/task/list";
        }
    }

    /**
     * 画面からのステータス文字列を DB 格納用コードへ変換
     */
    private String convertStatusToDbValue(String status) {
        if (status == null) return "pending";
        switch (status.trim()) {
            case "進行中":
            case "in_progress":
                return "in_progress";
            case "完了":
            case "completed":
                return "completed";
            case "未着手":
            case "pending":
            default:
                return "pending";
        }
    }

    /**
     * 画面からの優先度文字列を DB 格納用コードへ変換
     */
    private String convertPriorityToDbValue(String priority) {
        if (priority == null) return "medium";
        switch (priority.trim()) {
            case "高":
            case "high":
                return "high";
            case "低":
            case "low":
                return "low";
            case "中":
            case "medium":
            default:
                return "medium";
        }
    }

    private Task getTaskIfOwner(int taskId, int userId) throws SQLException {
        for (Task t : taskRepository.findByUserId(userId)) {
            if (t.getId() == taskId) {
                return t;
            }
        }
        return null;
    }
}