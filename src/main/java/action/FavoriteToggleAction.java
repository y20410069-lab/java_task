package action;

import java.io.IOException;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.User;
import repository.TaskRepository;

/**
 * タスクのお気に入り状態を切り替えるアクションクラスです。
 */
public class FavoriteToggleAction extends BaseAuthAction {

    private final TaskRepository taskRepository;

    public FavoriteToggleAction() {
        this.taskRepository = new TaskRepository();
    }

    public FavoriteToggleAction(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws ServletException, IOException {
        
        // リクエストパラメータ "taskId" を取得
        String taskIdStr = request.getParameter("taskId");
        if (taskIdStr == null || taskIdStr.trim().isEmpty()) {
            request.getSession().setAttribute("error", "不正なパラメータです。");
            return "redirect:/app/task/list";
        }

        try {
            int taskId = Integer.parseInt(taskIdStr); //
            if (taskId <= 0) {
                request.getSession().setAttribute("error", "無効なタスクIDです。");
                return "redirect:/app/task/list";
            }

            int userId = loginUser.getId(); //

            // 所有者チェック
            if (!taskRepository.isOwner(taskId, userId)) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "アクセス権限がありません。");
                return null;
            }

            // お気に入り状態を切り替え
            taskRepository.toggleFavorite(taskId, userId);

            // 処理成功時: 一覧画面にリダイレクト
            return "redirect:/app/task/list";
            
        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "タスクIDの形式が不正です。");
            return "redirect:/app/task/list";
        } catch (SQLException e) {
            System.err.println("[FavoriteToggleAction] DBエラー: " + e.getMessage());
            request.getSession().setAttribute("error", "お気に入り状態の更新に失敗しました。");
            return "redirect:/app/task/list";
        } catch (Exception e) {
            System.err.println("[FavoriteToggleAction] 予期せぬエラー: " + e.getMessage());
            request.getSession().setAttribute("error", "システムエラーが発生しました。");
            return "redirect:/app/task/list";
        }
    }
}