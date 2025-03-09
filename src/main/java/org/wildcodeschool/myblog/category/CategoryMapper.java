package org.wildcodeschool.myblog.category;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.article.ArticleDTO;

import java.util.stream.Collectors;
@Component
public class CategoryMapper {
    public CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        if(category.getArticles() != null) {
            categoryDTO.setArticles(category.getArticles().stream().map(article -> {
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setId(article.getId());
                articleDTO.setTitle(article.getTitle());
                articleDTO.setContent(article.getContent());
                articleDTO.setUpdatedAt(article.getUpdatedAt());
                articleDTO.setCategoryName(article.getCategory().getName());
                articleDTO.setImageUrls(article.getImages().stream().map(image -> image.getUrl()).collect(Collectors.toList()));
                return articleDTO;
            }).collect(Collectors.toList()));
        }

        return categoryDTO;
    }

    public Category convertToEntity(CategoryCreateDTO categoryCreateDTO) {
        Category category = new Category();
        return category;
    }
}
