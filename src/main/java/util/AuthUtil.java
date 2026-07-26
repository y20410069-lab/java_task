package util;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import model.User;

/**
 * 認可制御（アクセス権限のチェック）に関する共通処理を提供するユーティリティクラスです。
 */
public class AuthUtil {

    /**
     * セッションから現在ログインしているユーザーの情報を取得します。
     * * @param request HTTPリクエスト
     * @return ログインユーザーのUserオブジェクト。ログインしていない場合はnull
     */
    public static User getLoginUser(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return null;
        }
        try {
            return (User) session.getAttribute("loginUser");
        } catch (ClassCastException e) {
            // セッション情報が破損しているなどの場合、安全のために属性を削除してnullを返す
            session.removeAttribute("loginUser");
            return null;
        }
    }

    /**
     * ユーザーがログイン状態であるかを確認します。
     * * @param request HTTPリクエスト
     * @return ログインしている場合はtrue、そうでない場合はfalse
     */
    public static boolean isLoggedIn(HttpServletRequest request) {
        return getLoginUser(request) != null;
    }

    /**
     * 操作対象のデータが、ログインしているユーザー本人のものであるかをチェックします（データレベルの認可制御）。
     * * @param resourceOwnerId データの所有者のユーザーID
     * @param loginUserId ログインしているユーザーのID
     * @return 一致している場合はtrue、そうでない場合はfalse
     */
    public static boolean isOwner(int resourceOwnerId, int loginUserId) {
        return resourceOwnerId == loginUserId;
    }

    /**
     * ログイン状態とリソースの所有権をあわせてチェックする統合権限検証です。
     * * @param request HTTPリクエスト
     * @param resourceOwnerId データの所有者のユーザーID
     * @return アクセス権限がある場合はtrue、そうでない場合はfalse
     */
    public static boolean canAccess(HttpServletRequest request, int resourceOwnerId) {
        User loginUser = getLoginUser(request);
        if (loginUser == null) {
            return false;
        }
        return isOwner(resourceOwnerId, loginUser.getId());
    }

    /**
     * 未認証（未ログイン）のユーザーをログイン画面へ強制的にリダイレクトします。
     * * @param request HTTPリクエスト
     * @return ログイン画面へのリダイレクト指示文字列
     */
    public static String redirectToLogin(HttpServletRequest request) {
        return "redirect:/app/login";
    }
}