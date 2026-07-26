package database;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * タスク情報を保持するモデルクラス（JavaBean）
 */
public class Task implements Serializable {
    private static final long serialVersionUID = 1L;

    private int taskId;
    private int userId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private boolean favorite;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    // 引数なしコンストラクタ（JavaBeanの必須要件）
    public Task() {}

    // --- ID (taskId / id の両方のgetter/setterを用意してEL式エラーを防止) ---
    public int getTaskId() { 
        return taskId; 
    }
    public void setTaskId(int taskId) { 
        this.taskId = taskId; 
    }

    public int getId() { 
        return taskId; 
    }
    public void setId(int id) { 
        this.taskId = id; 
    }

    // --- ユーザーID ---
    public int getUserId() { 
        return userId; 
    }
    public void setUserId(int userId) { 
        this.userId = userId; 
    }

    // --- タイトル・説明 ---
    public String getTitle() { 
        return title; 
    }
    public void setTitle(String title) { 
        this.title = title; 
    }

    public String getDescription() { 
        return description; 
    }
    public void setDescription(String description) { 
        this.description = description; 
    }

    // --- ステータス・優先度 ---
    public String getStatus() { 
        return status; 
    }
    public void setStatus(String status) { 
        this.status = status; 
    }

    public String getPriority() { 
        return priority; 
    }
    public void setPriority(String priority) { 
        this.priority = priority; 
    }

    // --- お気に入り（isFavorite / getFavorite の両方を用意） ---
    public boolean isFavorite() { 
        return favorite; 
    }
    public boolean getFavorite() { 
        return favorite; 
    }
    public void setFavorite(boolean favorite) { 
        this.favorite = favorite; 
    }

    // --- 日時情報 ---
    public Timestamp getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(Timestamp createdAt) { 
        this.createdAt = createdAt; 
    }

    public Timestamp getUpdatedAt() { 
        return updatedAt; 
    }
    public void setUpdatedAt(Timestamp updatedAt) { 
        this.updatedAt = updatedAt; 
    }
}