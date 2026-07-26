package action;

import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import model.Task;
import model.User;
import repository.TaskRepository;

/**
 * タスク一覧の表示および検索・並べ替え・ページング処理を行うアクションクラスです。
 */
public class TaskListAction extends BaseAuthAction {

    private final TaskRepository taskRepository = new TaskRepository();
    private static final int PAGE_SIZE = 10; // 1ページあたりの表示件数

    @Override
    protected String executeAuthenticated(HttpServletRequest request, HttpServletResponse response, User loginUser)
            throws Exception {
        
        // パラメータ取得
        String keyword = request.getParameter("keyword");
        String sort = request.getParameter("sort");
        String pageParam = request.getParameter("page");

        if (sort == null || sort.isEmpty()) {
            sort = "DESC";
        }

        // ページ番号の安全な数値変換とバリデーション
        int page = 1;
        if (pageParam != null && pageParam.matches("\\d+")) {
            page = Integer.parseInt(pageParam);
            if (page < 1) {
                page = 1;
            }
        }

        try {
            int userId = loginUser.getId();
            
            // ① 総件数取得
            int totalRecords = taskRepository.countTasks(userId, keyword);
            
            // ② ページング計算（総ページ数、切り上げ・最小1ページ処理）
            int totalPages = (totalRecords + PAGE_SIZE - 1) / PAGE_SIZE;
            if (totalPages == 0) {
                totalPages = 1;
            }
            
            // 範囲外ページが指定された場合は1ページ目にフォールバック
            if (page > totalPages) {
                page = 1;
            }
            
            // ③ OFFSET値計算とデータ取得
            int offset = (page - 1) * PAGE_SIZE;
            List<Task> tasks = taskRepository.searchWithPaging(userId, keyword, sort, PAGE_SIZE, offset);
            
            // ④ 表示レコード範囲計算
            int startRecord = (totalRecords == 0) ? 0 : offset + 1;
            int endRecord = Math.min(offset + PAGE_SIZE, totalRecords);
            
            // ⑤ 結果・ページング情報をJSPに設定
            request.setAttribute("tasks", tasks);
            request.setAttribute("keyword", (keyword != null) ? keyword : "");
            request.setAttribute("sort", sort);
            request.setAttribute("currentPage", page);
            request.setAttribute("totalPages", totalPages);
            request.setAttribute("totalRecords", totalRecords);
            request.setAttribute("startRecord", startRecord);
            request.setAttribute("endRecord", endRecord);

            return "task/list";
            
        } catch (Exception e) {
            System.err.println("[TaskListAction] エラー: " + e.getMessage());
            request.setAttribute("error", "タスク一覧の取得に失敗しました。");
            request.setAttribute("tasks", List.of());
            request.setAttribute("currentPage", 1);
            request.setAttribute("totalPages", 1);
            request.setAttribute("totalRecords", 0);
            return "task/list";
        }
    }
}