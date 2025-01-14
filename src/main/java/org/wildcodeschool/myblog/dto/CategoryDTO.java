package org.wildcodeschool.myblog.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CategoryDTO {
    private Long id;
    private String name;
    private LocalDateTime updatedAt;
    private List<ArticleDTO> articles;

    // Getters and setters

    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id=id;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name=name;
    }
    public LocalDateTime getUpdatedAt(){
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt){
        this.updatedAt=updatedAt;
    }
    public List<ArticleDTO> getArticles(){
        return articles;
    }
    public void setArticles(List<ArticleDTO> articles){
        this.articles=articles;
    }
}
