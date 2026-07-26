package action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Action;

/**
 * 認可エラーなどの例外発生時に、エラー画面への遷移を制御するアクションクラスです。
 */
public class ErrorAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // エラー種別による分岐
        Integer statusCode = (Integer) request.getAttribute("jakarta.servlet.error.status_code");
        
        if (statusCode != null && statusCode == 403) {
            request.setAttribute("errorTitle", "アクセス権限がありません");
            request.setAttribute("errorMessage", "このページにアクセスする権限がありません。");
            request.setAttribute("suggestedAction", "適切な権限を持つアカウントでログインし直してください。");
        } else {
            request.setAttribute("errorTitle", "システムエラー");
            request.setAttribute("errorMessage", "予期せぬエラーが発生しました。");
            request.setAttribute("suggestedAction", "時間を置いてから再度お試しください。");
        }
        
        return "/WEB-INF/views/error/error.jsp";
    }
}