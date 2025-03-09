package org.wildcodeschool.myblog.article;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.articleAuthor.ArticleAuthor;
import org.wildcodeschool.myblog.articleAuthor.ArticleAuthorRepository;
import org.wildcodeschool.myblog.author.Author;
import org.wildcodeschool.myblog.author.AuthorRepository;
import org.wildcodeschool.myblog.category.Category;
import org.wildcodeschool.myblog.category.CategoryRepository;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.image.Image;
import org.wildcodeschool.myblog.image.ImageRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final AuthorRepository authorRepository;
    private final ArticleAuthorRepository articleAuthorRepository;

    public ArticleService(
            ArticleRepository articleRepository,
            ArticleMapper articleMapper,
            CategoryRepository categoryRepository,
            ImageRepository imageRepository,
            AuthorRepository authorRepository,
            ArticleAuthorRepository articleAuthorRepository) {
        this.articleRepository = articleRepository;
        this.articleMapper = articleMapper;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.authorRepository = authorRepository;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<ArticleDTO> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            throw new ResourceNotFoundException("No articles found");
        }
        return articles.stream().map(articleMapper::convertToDTO).collect(Collectors.toList());
    }

    public ArticleDTO getArticleById(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Article id" + id + "not found"));

        return articleMapper.convertToDTO(article);
    }

    public ArticleDTO createArticle(ArticleCreateDTO articleCreateDTO) {
        Article article = articleMapper.convertToEntity(articleCreateDTO);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());

        // Gestion de la catégorie
        if (article.getCategory() != null) {
            Category category = categoryRepository.findById(article.getCategory().getId()).orElseThrow(()->new ResourceNotFoundException("Category id" + article.getCategory().getId() + "not found"));
            article.setCategory(category);
        }

        // Gestion des images
        if (article.getImages() != null && !article.getImages().isEmpty()) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElseThrow(()->new ResourceNotFoundException("Image id" + image.getId() + "not found"));
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        }

        Article savedArticle = articleRepository.save(article);

        // Gestion des auteurs
        if (article.getArticleAuthors() != null) {
            for (ArticleAuthor articleAuthor : article.getArticleAuthors()) {
                Author author = articleAuthor.getAuthor();
                author = authorRepository.findById(author.getId()).orElseThrow(()-> new ResourceNotFoundException("Author id" + articleAuthor.getId() + "not found"));

                articleAuthor.setAuthor(author);
                articleAuthor.setArticle(savedArticle);
                articleAuthor.setContribution(articleAuthor.getContribution());

                articleAuthorRepository.save(articleAuthor);
            }
        }

        return articleMapper.convertToDTO(savedArticle);
    }

    public ArticleDTO updateArticle(Long id, ArticleUpdateDTO articleUpdateDTO) {

        Article dto = articleMapper.convertToEntity(articleUpdateDTO);
        Article article = articleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Article id" + id + "not found"));
        article.setTitle(dto.getTitle());
        article.setContent(dto.getContent());
        article.setUpdatedAt(LocalDateTime.now());

        // Mise à jour de la catégorie
        if (dto.getCategory() != null) {
            Category category = categoryRepository.findById(dto.getCategory().getId()).orElseThrow(()->new ResourceNotFoundException("Category id" + dto.getCategory().getId() + "not found"));
            article.setCategory(category);
        }

        // Mise à jour des images
        if (dto.getImages() != null) {
            List<Image> validImages = new ArrayList<>();
            for (Image image : dto.getImages()) {
                if (image.getId() != null) {
                    Image existingImage = imageRepository.findById(image.getId()).orElseThrow(()->new ResourceNotFoundException("Image id" + image.getId() + "not found"));
                    if (existingImage != null) {
                        validImages.add(existingImage);
                    }
                } else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        // Mise à jour des auteurs
        if (dto.getArticleAuthors() != null) {
            for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for (ArticleAuthor articleAuthorDetails : dto.getArticleAuthors()) {
                Author author = articleAuthorDetails.getAuthor();
                author = authorRepository.findById(author.getId()).orElseThrow(()-> new ResourceNotFoundException("Author id" + articleAuthorDetails.getId() + "not found"));

                ArticleAuthor newArticleAuthor = new ArticleAuthor();
                newArticleAuthor.setAuthor(author);
                newArticleAuthor.setArticle(article);
                newArticleAuthor.setContribution(articleAuthorDetails.getContribution());

                updatedArticleAuthors.add(newArticleAuthor);
            }

            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                articleAuthorRepository.save(articleAuthor);
            }

            article.setArticleAuthors(updatedArticleAuthors);
        }

        Article updatedArticle = articleRepository.save(article);
        return articleMapper.convertToDTO(updatedArticle);
    }

    public void deleteArticle(Long id) {
        Article article = articleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Article id" + id + "not found"));
        articleAuthorRepository.deleteAll(article.getArticleAuthors());
        articleRepository.delete(article);
    }
}
