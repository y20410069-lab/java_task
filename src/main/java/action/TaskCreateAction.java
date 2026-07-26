package action;

import java.sql.SQLException;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Task;
import model.User;
import repository.TaskRepository;

/**
 * タスクの新規登録処理を行うアクションクラスです。
 */
public class TaskCreateAction extends BaseAuthAction {

    private final TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws Exception {
        
        // POSTリクエスト以外は新規登録フォームへリダイレクトまたはフォワード
        if (!"POST".equals(request.getMethod())) {
            return "redirect:/java_task/app/task/new";
        }

        // フォームパラメータの取得
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");

        // 🚀 サーバーサイドバリデーション
        if (title == null || title.trim().isEmpty()) {
            request.setAttribute("error", "タイトルは必須です。");
            setFormAttributes(request, title, description, status, priority);
            return "task/new";
        }
        if (title.length() > 200) {
            request.setAttribute("error", "タイトルは200文字以内で入力してください。");
            setFormAttributes(request, title, description, status, priority);
            return "task/new";
        }

        // タスクオブジェクトの構築
        Task task = new Task();
        task.setUserId(loginUser.getId());
        task.setTitle(title.trim());
        task.setDescription(description != null ? description.trim() : "");
        task.setStatus(status != null && !status.trim().isEmpty() ? status : "未着手");
        task.setPriority(priority != null && !priority.trim().isEmpty() ? priority : "中");

        try {
            taskRepository.save(task);
            return "redirect:/java_task/app/task/list";
        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベースエラーが発生しました。");
            setFormAttributes(request, title, description, status, priority);
            return "task/new";
        }
    }

    /**
     * エラー時にフォームの入力値を保持させるためのヘルパーメソッド
     */
    private void setFormAttributes(HttpServletRequest request, String title, String description, String status, String priority) {
        Task task = new Task();
        task.setTitle(title);
        task.setDescription(description);
        task.setStatus(status);
        task.setPriority(priority);
        request.setAttribute("task", task);
    }
}