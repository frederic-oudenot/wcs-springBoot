package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleAuthorDTO;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.AuthorDTO;
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
        try {
            List<Article> articles = articleRepository.findAll();
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<ArticleDTO> articleDTOs = articles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articleDTOs);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ ONE ARTICLE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<ArticleDTO> getArticleById(@PathVariable Long id){
        try{
            Article article = articleRepository.findById(id).orElse(null);
            if(article == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(convertoDTO(article));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ FIND ARTICLE BY TITLE
     * */
    @GetMapping("/search-title")
    ResponseEntity<List<ArticleDTO>> getArticlesByTitle(@RequestParam String searchTerms){
        try {
            List<Article> foundArticles = articleRepository.findByTitle(searchTerms);
            if(foundArticles.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articlesDTOs);
        }catch(Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }

    /**
     * READ FIND ARTICLE BY CONTENT
     * */
    @GetMapping("/search-content")
    ResponseEntity<List<ArticleDTO>> getArticlesByContent(@RequestParam String searchTerms){
        try{
            List<Article> foundArticles = articleRepository.findByContentContaining(searchTerms);
            if(foundArticles.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articlesDTOs);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ FIND ARTICLE AFTER A DATE TIME
     * */
    @GetMapping("/search-date")
    ResponseEntity<List<ArticleDTO>> getArticlesAfterDate(@RequestParam LocalDateTime date){
        try {
            List<Article> foundArticles = articleRepository.findByCreatedAtAfter(date);
            if (foundArticles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articlesDTOs);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ FIND ARTICLE LAST FIVE ARTICLES
     * */
    @GetMapping("/last-articles")
    ResponseEntity<List<ArticleDTO>> getLastFiveArticles(){
        try{
            List<Article> foundArticles = articleRepository.findTop5ByOrderByCreatedAtDesc();
            if (foundArticles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            List<ArticleDTO> articlesDTOs = foundArticles.stream().map(this::convertoDTO).collect(Collectors.toList());
            return ResponseEntity.ok(articlesDTOs);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * CREATE ARTICLE
     * */
    @PostMapping()
    public ResponseEntity<ArticleDTO> createArticle(@RequestBody Article article){
        System.out.println(article.getImages());

        try {
            if(article.getCategory() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            Category foundCategory = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if(foundCategory == null){
                return ResponseEntity.badRequest().body(null);
            }

            if(foundCategory.getId() != article.getCategory().getId()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if(!foundCategory.getName().equals(article.getCategory().getName())){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

            if(article.getImages() != null && !article.getImages().isEmpty()){
                List<Image> validImages = new ArrayList<>();
                for(Image image : article.getImages()){
                    if(image.getId() != null){
                    Image foundImage = imageRepository.findById(image.getId()).orElse(null);
                    if(foundImage == null){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
                            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                        }
                        articleAuthor.setAuthor(articleAuthor.getAuthor());
                        articleAuthor.setArticle(savedArticle);
                        articleAuthor.setContribution(foundAuthor.getContribution());

                        articleAuthorRepository.save(articleAuthor);
                    }
                }
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(convertoDTO(savedArticle));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * UPDATE ARTICLE
     * */
    @PutMapping("/{id}")
    public ResponseEntity<ArticleDTO> updateArticle(@PathVariable Long id, @RequestBody Article article){
        try {
        Article foundArticle = this.articleRepository.findById(id).orElse(null);
        if(foundArticle==null){
            return ResponseEntity.notFound().build();
        }
        if(article.getCategory() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Category foundCategory = categoryRepository.findById(article.getCategory().getId()).orElse(null);

            if(foundCategory.getId() != article.getCategory().getId()){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            if(!foundCategory.getName().equals(article.getCategory().getName())){
                System.out.println(foundCategory.getName() + article.getCategory().getName());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }

        if(foundCategory == null){
                return ResponseEntity.badRequest().body(null);
            }
        if(article.getImages() != null){
            System.out.println("la");
            List<Image> validImages = new ArrayList<>();
            for (Image image : article.getImages()) {
                if(image.getId() != null){
                    Image foundImage = imageRepository.findById(image.getId()).orElse(null);
                    if(foundImage == null){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
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
                    ArticleAuthor foundAuthor = articleAuthorRepository.findById(articleAuthor.getId()).orElse(null);
                    if(foundAuthor == null){
                        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
                    }
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
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE ARTICLE
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArticle(@PathVariable Long id){
        try {
        Article foundArticle = this.articleRepository.findById(id).orElse(null);
        if(foundArticle == null){
            return ResponseEntity.notFound().build();
        }
        if(foundArticle.getArticleAuthors() != null){
            for(ArticleAuthor articleAuthor : foundArticle.getArticleAuthors()){
                articleAuthorRepository.delete(articleAuthor);
            }
        }
        articleRepository.delete(foundArticle);
        return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
