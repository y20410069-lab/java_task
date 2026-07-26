package util;

import java.util.ArrayList;
import java.util.List;

public class ValidationUtil {

    // タイトルの検証メソッド
    public static List<String> validateTitle(String title) {
        List<String> errors = new ArrayList<>();
        if (title == null || title.trim().isEmpty()) {
            errors.add("タイトルは必須です");
        } else {
            if (title.trim().length() < 2) {
                errors.add("タイトルは2文字以上で入力してください");
            }
            if (title.length() > 200) {
                errors.add("タイトルは200文字以内で入力してください");
            }
        }
        return errors;
    }

    // 説明の検証メソッド
    public static List<String> validateDescription(String description) {
        List<String> errors = new ArrayList<>();
        if (description != null && description.length() > 1000) {
            errors.add("説明は1000文字以内で入力してください");
        }
        return errors;
    }

    // ステータスの検証メソッド（日本語および英語表記の両方に対応）
    public static List<String> validateStatus(String status) {
        List<String> errors = new ArrayList<>();
        if (status != null && !status.trim().matches("未着手|進行中|完了|pending|in_progress|completed")) {
            errors.add("無効なステータスです");
        }
        return errors;
    }

    // 優先度の検証メソッド（日本語および英語表記の両方に対応）
    public static List<String> validatePriority(String priority) {
        List<String> errors = new ArrayList<>();
        if (priority != null && !priority.trim().matches("低|中|高|low|medium|high")) {
            errors.add("無効な優先度です");
        }
        return errors;
    }
}