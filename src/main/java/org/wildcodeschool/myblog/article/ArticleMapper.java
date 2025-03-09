package org.wildcodeschool.myblog.article;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.author.AuthorDTO;
import org.wildcodeschool.myblog.articleAuthor.ArticleAuthor;
import org.wildcodeschool.myblog.image.Image;
import org.wildcodeschool.myblog.image.ImageRepository;

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

    public Article convertToEntity(ArticleCreateDTO articleCreateDTO ) {

        Article article = new Article();
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

    public Article convertToEntity(ArticleUpdateDTO articleUpdateDTO ) {

        Article article = new Article();
        article.setTitle(articleUpdateDTO.getTitle());
        article.setContent(articleUpdateDTO.getContent());
        if(articleUpdateDTO.getImages() != null) {
            article.setImages(articleUpdateDTO.getImages().stream().map(ImageCreatedDTO->new Image()).collect(Collectors.toList()));
        }
        if(articleUpdateDTO.getAuthors() != null) {
            article.setArticleAuthors(articleUpdateDTO.getAuthors().stream().map(authorContributionDTO->new ArticleAuthor()).collect(Collectors.toList()));
        }
        return article;
    }

}
