package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    public final ArticleRepository articleRepository;
    public final CategoryRepository categoryRepository;
    public ArticleController(ArticleRepository articleRepository, CategoryRepository categoryRepository) {
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }
    /**
     * READ ALL ARTICLES
     * */
    @GetMapping()
    public ResponseEntity<List<Article>> getAllArticles() {
        try {
            List<Article> articles = articleRepository.findAll();
            if (articles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(articles);

        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ ONE ARTICLE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id){
        try{
            Article article = articleRepository.findById(id).orElse(null);
            if(article == null){
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(article);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ FIND ARTICLE BY TITLE
     * */
    @GetMapping("/search-title")
    ResponseEntity<List<Article>> getArticlesByTitle(@RequestParam String searchTerms){
        try {
            List<Article> foundArticles = articleRepository.findByTitle(searchTerms);
            if(foundArticles.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(foundArticles);
        }catch(Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
    }

    /**
     * READ FIND ARTICLE BY CONTENT
     * */
    @GetMapping("/search-content")
    ResponseEntity<List<Article>> getArticlesByContent(@RequestParam String searchTerms){
        try{
            List<Article> foundArticles = articleRepository.findByContentContaining(searchTerms);
            if(foundArticles.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(foundArticles);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ FIND ARTICLE AFTER A DATE TIME
     * */
    @GetMapping("/search-date")
    ResponseEntity<List<Article>> getArticlesAfterDate(@RequestParam LocalDateTime date){
        try {
            List<Article> foundArticles = articleRepository.findByCreatedAtAfter(date);
            if (foundArticles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(foundArticles);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * READ FIND ARTICLE LAST FIVE ARTICLES
     * */
    @GetMapping("/last-articles")
    ResponseEntity<List<Article>> getLastFiveArticles(){
        try{
            List<Article> foundArticles = articleRepository.findTop5ByOrderByCreatedAtDesc();
            if (foundArticles.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(foundArticles);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * CREATE ARTICLE
     * */
    @PostMapping()
    public ResponseEntity<Article> createArticle(@RequestBody Article article){
        try {
            if(article.getCategory() == null){
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
            Category foundCategory = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            System.out.println(foundCategory);

            if(foundCategory == null){
                return ResponseEntity.badRequest().body(null);
            }
            article.setCategory(foundCategory);
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * UPDATE ARTICLE
     * */
    @PatchMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article){
        try {
        Article foundArticle = this.articleRepository.findById(id).orElse(null);
        if(foundArticle==null){
            return ResponseEntity.notFound().build();
        }
        if(article.getCategory() == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        Category foundCategory = categoryRepository.findById(article.getCategory().getId()).orElse(null);
            if(foundCategory == null){
                return ResponseEntity.badRequest().body(null);
            }
        article.setCategory(foundCategory);
        foundArticle.setTitle(article.getTitle());
        foundArticle.setContent(article.getContent());
        foundArticle.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(foundArticle);
        return ResponseEntity.status(HttpStatus.OK).body(savedArticle);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * DELETE ARTICLE
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Long id){
        try {
        Article foundArticle = this.articleRepository.findById(id).orElse(null);
        if(foundArticle == null){
            return ResponseEntity.notFound().build();
        }
        articleRepository.delete(foundArticle);
        return ResponseEntity.noContent().build();
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
