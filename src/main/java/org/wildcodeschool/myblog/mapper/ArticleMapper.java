package org.wildcodeschool.myblog.mapper;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.dto.*;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.model.Image;
import org.wildcodeschool.myblog.repository.ImageRepository;

import java.util.stream.Collectors;

@Component
public class ArticleMapper {

    private final ImageRepository imageRepository;

    public ArticleMapper(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    public ArticleDTO convertToDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if (article.getCategory() != null) {
            articleDTO.setCategoryName(article.getCategory().getName());
        }
        if (article.getImages() != null) {
            articleDTO.setImageUrls(article.getImages().stream().map(Image::getUrl).collect(Collectors.toList()));
        }
        if (article.getArticleAuthors() != null) {
            articleDTO.setAuthors(article.getArticleAuthors().stream()
                    .filter(articleAuthor -> articleAuthor.getAuthor() != null)
                    .map(articleAuthor -> {
                        AuthorDTO authorDTO = new AuthorDTO();
                        authorDTO.setId(articleAuthor.getAuthor().getId());
                        authorDTO.setFirstName(articleAuthor.getAuthor().getFirstName());
                        authorDTO.setLastName(articleAuthor.getAuthor().getLastName());
                        return authorDTO;
                    })
                    .collect(Collectors.toList()));
        }
        return articleDTO;
    }

    public Article convertToEntity(ArticleCreateDTO articleCreateDTO) {

        Article article = new Article();
        Image image = new Image();
        article.setTitle(articleCreateDTO.getTitle());
        article.setContent(articleCreateDTO.getContent());
        if(articleCreateDTO.getImages() != null) {
            article.setImages(articleCreateDTO.getImages().stream().map(ImageCreatedDTO->new Image()).collect(Collectors.toList()));
        }
        if(articleCreateDTO.getAuthors() != null) {
            article.setArticleAuthors(articleCreateDTO.getAuthors().stream().map(authorContributionDTO->new ArticleAuthor()).collect(Collectors.toList()));
        }

        return article;
    }
}
