package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.BadRequestException;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.model.*;
import org.wildcodeschool.myblog.repository.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    public final ArticleRepository articleRepository;
    public final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ArticleAuthorRepository articleAuthorRepository;
    private final AuthorRepository authorRepository;

    public ArticleController(ArticleRepository articleRepository, CategoryRepository categoryRepository, ImageRepository imageRepository, ArticleAuthorRepository articleAuthorRepository, AuthorRepository authorRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
        this.imageRepository = imageRepository;
        this.articleAuthorRepository = articleAuthorRepository;
        this.authorRepository = authorRepository;
    }
    private ArticleDTO convertoDTO(Article article) {
        ArticleDTO articleDTO = new ArticleDTO();
        articleDTO.setId(article.getId());
        articleDTO.setTitle(article.getTitle());
        articleDTO.setContent(article.getContent());
        articleDTO.setUpdatedAt(article.getUpdatedAt());
        if(article.getCategory() != null) {
        articleDTO.setCategoryName(article.getCategory().getName());
        }
        if(article.getImages() != null) {
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

    /**
     * READ ALL ARTICLES
     * */
    @GetMapping()
    public ResponseEntity<List<ArticleDTO>> getAllArticles() {
        List<Article> articles = articleRepository.findAll();
        if (articles.isEmpty()) {
            new ResourceNotFoundException("No articles found");
        }
        List<ArticleDTO> articleDTOs = articles.stream().map(this::convertoDTO).collect(Collectors.toList());
        return ResponseEntity.ok(articleDTOs);
    }

    /**
     * READ ONE ARTICLE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id){
        Article article = articleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Article id: " + id + " not found"));
        return ResponseEntity.ok(convertoDTO(article));
    }

    /**
     * READ FIND ARTICLE BY TITLE
     * */
    @GetMapping("/search-title")
    ResponseEntity<List<ArticleDTO>> getArticlesByTitle(@RequestParam String searchTerms){
        if(searchTerms.isEmpty()){
            throw new BadRequestException("Search terms cannot be empty");
        }
        List<Article> foundArticles = articleRepository.findByTitle(searchTerms);
        System.out.println(foundArticles);
        if(foundArticles.isEmpty() ){
            new ResourceNotFoundException("Not found any article with title : " + searchTerms);
        }
        List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
        return ResponseEntity.ok(articlesDTOs);
    }

    /**
     * READ FIND ARTICLE BY CONTENT
     * */
    @GetMapping("/search-content")
    ResponseEntity<List<ArticleDTO>> getArticlesByContent(@RequestParam String searchTerms){
            if(searchTerms.isEmpty()){
                throw new BadRequestException("Search terms cannot be empty");
            }
            List<Article> foundArticles = articleRepository.findByContentContaining(searchTerms);
            if(foundArticles.isEmpty()){
                new ResourceNotFoundException("Not found any article with content : " + searchTerms);
            }
            List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articlesDTOs);
    }

    /**
     * READ FIND ARTICLE AFTER A DATE TIME
     * */
    @GetMapping("/search-date")
    ResponseEntity<List<ArticleDTO>> getArticlesAfterDate(@RequestParam(required = false) LocalDateTime date){
        if(date == null){
            throw new BadRequestException("date cannot be empty");
        }
        List<Article> foundArticles = articleRepository.findByCreatedAtAfter(date);
        if (foundArticles.isEmpty()) {
            new ResourceNotFoundException("Not found any article with date: " + date);
        }
        List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
        return ResponseEntity.ok(articlesDTOs);

    }

    /**
     * READ FIND ARTICLE LAST FIVE ARTICLES
     * */
    @GetMapping("/last-articles")
    ResponseEntity<List<ArticleDTO>> getLastFiveArticles(){
            List<Article> foundArticles = articleRepository.findTop5ByOrderByCreatedAtDesc();
            if (foundArticles.isEmpty()) {
                new ResourceNotFoundException("No articles found");
            }
            List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articlesDTOs);
    }

    /**
     * CREATE ARTICLE
     * */
    @PostMapping()
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article){

            if(article.getCategory() == null){
                throw new BadRequestException("Category cannot be empty");
            }

            Category foundCategory = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if(foundCategory == null){
                throw new BadRequestException("Category not found");
            }

            if(foundCategory.getId() != article.getCategory().getId()){
                throw new BadRequestException("Category id mismatch");
            }

            if(!foundCategory.getName().equals(article.getCategory().getName())){
                throw new BadRequestException("Category name mismatch");
            }

            if(article.getImages() != null && !article.getImages().isEmpty()){
                List<Image> validImages = new ArrayList<>();
                for(Image image : article.getImages()){
                    if(image.getId() != null){
                    Image foundImage = imageRepository.findById(image.getId()).orElse(null);
                    if(foundImage == null){
                        throw new BadRequestException("Image not found");
                    } else {
                        validImages.add(image);
                    }
                    } else {
                        Image savedImage = imageRepository.save(image);
                        validImages.add(savedImage);
                    }
                }
                    article.setImages(validImages);
            }


            article.setCategory(foundCategory);
            article.setCreatedAt(LocalDateTime.now());
            article.setUpdatedAt(LocalDateTime.now());
            Article savedArticle = articleRepository.save(article);

            if(article.getArticleAuthors() != null){
                for(ArticleAuthor articleAuthor : article.getArticleAuthors()){
                    if(articleAuthor.getId() != null){
                        ArticleAuthor foundAuthor = articleAuthorRepository.findById(articleAuthor.getId()).orElse(null);
                        if(foundAuthor == null){
                            throw new BadRequestException("Author not found");
                        }
                        articleAuthor.setAuthor(articleAuthor.getAuthor());
                        articleAuthor.setArticle(savedArticle);
                        articleAuthor.setContribution(foundAuthor.getContribution());

                        articleAuthorRepository.save(articleAuthor);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(convertoDTO(savedArticle));
    }

    /**
     * UPDATE ARTICLE
     * */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article article){

        if(article.getCategory() == null){
            throw new BadRequestException("Category cannot be empty");
        }

        Article foundArticle = this.articleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Article id: " + id + " not found"));
        if(foundArticle==null){
            throw new ResourceNotFoundException("Article id: " + id + " not found");
        }
        Category foundCategory = categoryRepository.findById(article.getCategory().getId()).orElse(null);

        if(foundCategory == null) {
            throw new BadRequestException("Category not found");
        }
        if(foundCategory.getId() != article.getCategory().getId()){
            throw new BadRequestException("Category id mismatch");
        }
        if(!foundCategory.getName().equals(article.getCategory().getName())){
            throw new BadRequestException("Category name mismatch");
        }

        if(article.getImages() != null){
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if(image.getId() != null){
                    Image foundImage = imageRepository.findById(image.getId()).orElse(null);
                    if(foundImage == null){
                        throw new BadRequestException("Image not found");
                    } else {
                        validImages.add(image);
                    }
                }else {
                    Image savedImage = imageRepository.save(image);
                    validImages.add(savedImage);
                }
            }
            article.setImages(validImages);
        } else {
            article.getImages().clear();
        }

        if(article.getArticleAuthors() != null){
            for (ArticleAuthor oldArticleAuthor : article.getArticleAuthors()) {
                articleAuthorRepository.delete(oldArticleAuthor);
            }

            List<ArticleAuthor> updatedArticleAuthors = new ArrayList<>();

            for(ArticleAuthor articleAuthor : article.getArticleAuthors()){
                if(articleAuthor.getId() != null){
                    ArticleAuthor foundAuthor = articleAuthorRepository.findById(articleAuthor.getId()).orElseThrow(()->new ResourceNotFoundException("Article author id: " + articleAuthor.getId() + " not found"));
                    ArticleAuthor newArticleAuthor = new ArticleAuthor();
                    newArticleAuthor.setAuthor(articleAuthor.getAuthor());
                    newArticleAuthor.setArticle(article);
                    newArticleAuthor.setContribution(foundAuthor.getContribution());

                    updatedArticleAuthors.add(newArticleAuthor);
                }
            }
            for (ArticleAuthor articleAuthor : updatedArticleAuthors) {
                articleAuthorRepository.save(articleAuthor);
            }

            article.setArticleAuthors(updatedArticleAuthors);
        }

        article.setCategory(foundCategory);
        foundArticle.setTitle(article.getTitle());
        foundArticle.setContent(article.getContent());
        foundArticle.setUpdatedAt(LocalDateTime.now());

        Article savedArticle = articleRepository.save(foundArticle);

        return ResponseEntity.status(HttpStatus.OK).body(convertoDTO(savedArticle));

    }

    /**
     * DELETE ARTICLE
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){

        Article foundArticle = this.articleRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Article id: " + id + " not found"));

        if(foundArticle.getArticleAuthors() != null){
            for(ArticleAuthor articleAuthor : foundArticle.getArticleAuthors()){
                articleAuthorRepository.delete(articleAuthor);
            }
        }
        articleRepository.delete(foundArticle);
        return ResponseEntity.noContent().build();
    }
}
