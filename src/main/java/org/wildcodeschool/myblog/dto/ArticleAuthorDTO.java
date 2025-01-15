package org.wildcodeschool.myblog.dto;

import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Author;

import java.util.List;

public class ArticleAuthorDTO {

    private Long id;
    private String contribution;

    private Long authorIds;
    private Long articleIds;

    // Getters et setters


    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getContribution() {
        return contribution;
    }
    public void setContribution(String contribution) {
        this.contribution = contribution;
    }

    public Long getAuthorIds() {
        return authorIds;
    }
    public void setAuthorIds(Long authorIds) {
        this.authorIds = authorIds;
    }
    public Long getArticleIds() {
        return articleIds;
    }
    public void setArticleIds(Long articleIds) {
        this.articleIds = articleIds;
    }

}
