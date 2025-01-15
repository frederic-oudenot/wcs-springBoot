package org.wildcodeschool.myblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ImageDTO {
    private Long id;
    private String url;
    private LocalDateTime updatedAt;
    private List<Long> articleIds;

    // Getters et setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    public List<Long> getArticleIds() {
        return articleIds;
    }
    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }
}
