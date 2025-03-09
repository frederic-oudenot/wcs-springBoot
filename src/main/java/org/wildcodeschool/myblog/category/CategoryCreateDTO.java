package org.wildcodeschool.myblog.category;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.wildcodeschool.myblog.article.ArticleCreateDTO;

import java.util.List;

public class CategoryCreateDTO {
    @NotBlank(message = "Le nom de la catégorie ne doit pas être vide")
    @Size(min=2, max=50, message = "Le nom de la catégorie doit contenir entre 2 et 50 caractères")
    private String name;

    private List<@Valid ArticleCreateDTO> articles;

    public String getName() {
        return name;
    }

    public List<ArticleCreateDTO> getArticles() {
        return articles;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArticles(List<ArticleCreateDTO> articles) {
        this.articles = articles;
    }
}
