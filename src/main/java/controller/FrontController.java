package controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import action.FavoriteToggleAction;
import action.HelloWorldAction;
import action.LoginAction;
import action.LogoutAction;
import action.TaskDeleteAction;
import action.TaskEditAction;
import action.TaskListAction;
import action.TaskNewAction;

/**
 * すべてのリクエスト（/app/*）を集中管理し、適切なActionへ振り分けるフロントコントローラーです。
 */
@WebServlet("/app/*")
public class FrontController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
    // Actionインターフェース型で安全に管理
    private final Map<String, Action> actionMap = new HashMap<>();

    @Override
    public void init() throws ServletException {
        // パスの先頭に「/」を含めて正確にマッピング
        actionMap.put("/hello", new HelloWorldAction()); // 動作確認用
        actionMap.put("/login", new LoginAction());
        actionMap.put("/logout", new LogoutAction());
        actionMap.put("/task/list", new TaskListAction());
        actionMap.put("/task/new", new TaskNewAction()); 
        actionMap.put("/task/edit", new TaskEditAction());
        actionMap.put("/task/delete", new TaskDeleteAction());
        actionMap.put("/favorite/toggle", new FavoriteToggleAction()); // お気に入り切り替え用アクションの登録
        
        System.out.println("[FrontController] 初期化完了: " + actionMap.size() + "件のアクションが登録されました。");
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");
        
        String pathInfo = request.getPathInfo();
        
        // /app/ のみのアクセスやパスがnullの場合は /hello にフォールバック
        if (pathInfo == null || pathInfo.equals("/")) {
            pathInfo = "/hello";
        }

        // マップからActionを取得
        Action action = actionMap.get(pathInfo);

        if (action == null) {
            System.out.println("[FrontController] マッチするActionが見つかりません: " + pathInfo);
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            String result = action.execute(request, response);
            
            if (result == null) {
                return;
            }

            if (result.startsWith("redirect:")) {
                String redirectPath = result.substring("redirect:".length());
                
                // 相対パス記法（../や./）や重複するコンテキストパス(/java_task)を安全に除去・整形
                redirectPath = redirectPath.replace("../", "").replace("./", "");
                if (redirectPath.startsWith(request.getContextPath())) {
                    redirectPath = redirectPath.substring(request.getContextPath().length());
                }
                if (!redirectPath.startsWith("/")) {
                    redirectPath = "/" + redirectPath;
                }
                
                // 必ず [ContextPath] + [/app/...] の正解URLを生成してリダイレクト
                response.sendRedirect(request.getContextPath() + redirectPath);
            } else {
                // "/WEB-INF/views/" と ".jsp" を自動付与
                String jspPath = "/WEB-INF/views/" + result + ".jsp";
                request.getRequestDispatcher(jspPath).forward(request, response);
            }
            
        } catch (Exception e) {
            System.err.println("[FrontController] 実行エラー: " + e.getMessage());
            e.printStackTrace();
            throw new ServletException(e);
        }
    }
}