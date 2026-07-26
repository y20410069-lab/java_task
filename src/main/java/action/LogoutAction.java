package action;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import controller.Action;
import util.SessionUtil;

/**
 * ログアウト処理を行うアクションクラスです。
 */
public class LogoutAction implements Action {

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) throws Exception {
        SessionUtil.logout(request);
        return "redirect:/app/login";
    }
}