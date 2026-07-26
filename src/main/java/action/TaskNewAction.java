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
import util.ValidationUtil; // ValidationUtilのインポート

/**
 * タスクの新規作成処理を行うアクションクラスです。
 */
public class TaskNewAction extends BaseAuthAction {

    private final TaskRepository taskRepository = new TaskRepository();

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws Exception {
        
        String method = request.getMethod();

        if ("POST".equals(method)) {
            return processNewTask(request, loginUser);
        }
        
        return showNewForm(request);
    }

    private String showNewForm(HttpServletRequest request) {
        // new.jsp を返す
        return "task/new";
    }

    private String processNewTask(HttpServletRequest request, User loginUser) {
        // ① フォームデータ取得
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String status = request.getParameter("status");
        String priority = request.getParameter("priority");

        // ② フィールド別エラーマップの初期化
        Map<String, List<String>> fieldErrors = new HashMap<>();

        // ③ ValidationUtilによる詳細バリデーション実行
        List<String> titleErrors = ValidationUtil.validateTitle(title);
        if (!titleErrors.isEmpty()) {
            fieldErrors.put("title", titleErrors);
        }

        List<String> descErrors = ValidationUtil.validateDescription(description);
        if (!descErrors.isEmpty()) {
            fieldErrors.put("description", descErrors);
        }

        List<String> statusErrors = ValidationUtil.validateStatus(status);
        if (!statusErrors.isEmpty()) {
            fieldErrors.put("status", statusErrors);
        }

        List<String> priorityErrors = ValidationUtil.validatePriority(priority);
        if (!priorityErrors.isEmpty()) {
            fieldErrors.put("priority", priorityErrors);
        }

        // ④ バリデーションエラーがある場合、入力値を保持してフォーム再表示
        if (!fieldErrors.isEmpty()) {
            request.setAttribute("errors", fieldErrors);
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("status", status);
            request.setAttribute("priority", priority);
            return "task/new";
        }

        try {
            // ⑤ Taskオブジェクト作成・設定
            Task task = new Task();
            task.setUserId(loginUser.getId());
            task.setTitle(title);
            task.setDescription(description);
            task.setStatus(status != null ? status : "未着手");
            task.setPriority(priority != null ? priority : "中");

            // ⑥ データベースに保存
            boolean success = taskRepository.save(task);

            if (success) {
                // ⑦ 成功時: FrontControllerが自動で ContextPath を補完するため /app から記述
                return "redirect:/app/task/list";
            } else {
                request.setAttribute("error", "タスクの保存に失敗しました。");
                request.setAttribute("title", title);
                request.setAttribute("description", description);
                request.setAttribute("status", status);
                request.setAttribute("priority", priority);
                return "task/new";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            request.setAttribute("error", "データベースエラーが発生しました。");
            request.setAttribute("title", title);
            request.setAttribute("description", description);
            request.setAttribute("status", status);
            request.setAttribute("priority", priority);
            return "task/new";
        }
    }
}