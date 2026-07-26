package action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Action;
import model.User;
import util.AuthUtil;

/**
 * 認証チェックを必須とするアクションクラスの基底となる抽象クラスです。
 */
public abstract class BaseAuthAction implements Action {

    @Override
    public final String execute(HttpServletRequest request, HttpServletResponse response) 
            throws Exception {
        
        // 1. ログイン状態のチェック
        User loginUser = AuthUtil.getLoginUser(request);
        
        if (loginUser == null) {
            // 未ログインの場合はログイン画面へリダイレクト
            return AuthUtil.redirectToLogin(request);
        }
        
        // 2. 認証済みの場合のみ、サブクラスで定義された具体的な業務処理を実行
        return executeAuthenticated(request, response, loginUser);
    }

    /**
     * 認証が成功した後に実行される具体的な業務処理です。
     * サブクラス（各Actionクラス）でこのメソッドをオーバーライドして実装します。
     * * @param request HTTPリクエスト
     * @param response HTTPレスポンス
     * @param loginUser ログイン中のユーザー情報
     * @return 遷移先のパス、またはリダイレクト指示
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     * @throws Exception その他の例外
     */
    protected abstract String executeAuthenticated(
            HttpServletRequest request, 
            HttpServletResponse response, 
            User loginUser) throws Exception;
}