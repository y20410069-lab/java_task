package model;

import java.sql.Timestamp;

public class Task {
    private int id;
    private int userId;
    private String title;
    private String description;
    private String status;
    private String priority;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private boolean isFavorite; // 【新規追加】お気に入りフラグ

    public Task() {}

    public Task(int id, int userId, String title, String description, String status, String priority, Timestamp createdAt, Timestamp updatedAt, boolean isFavorite) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isFavorite = isFavorite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    // 【新規追加】お気に入りフラグのGetter/Setter
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean isFavorite) { this.isFavorite = isFavorite; }

    @Override
    public String toString() {
        return "Task [id=" + id + ", userId=" + userId + ", title=" + title + ", status=" + status + ", isFavorite=" + isFavorite + "]";
    }
}