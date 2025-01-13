package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.repository.ArticleRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    public final ArticleRepository articleRepository;
    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }
    /**
     * READ ALL ARTICLES
     * */
    @GetMapping()
    public ResponseEntity<List<Article>> getAllArticles(){
    List<Article> articles = articleRepository.findAll();
    if(articles.isEmpty()){
    return ResponseEntity.noContent().build();
    }
    return ResponseEntity.ok(articles);
    }
    /**
     * READ ONE ARTICLE
     * */
    @GetMapping("/{id}")
    public ResponseEntity<Article> getArticleById(@PathVariable Long id){
    Article article = articleRepository.findById(id).orElse(null);
    if(article == null){
        return ResponseEntity.notFound().build();
    }
    return ResponseEntity.ok(article);
    }
    /**
     * CREATE ARTICLE
     * */
    @PostMapping()
    public ResponseEntity<Article> createArticle(@RequestBody Article article){
        article.setCreatedAt(LocalDateTime.now());
        article.setUpdatedAt(LocalDateTime.now());
        Article savedArticle = articleRepository.save(article);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedArticle);
    }
    /**
     * UPDATE ARTICLE
     * */
    @PatchMapping("/{id}")
    public ResponseEntity<Article> updateArticle(@PathVariable Long id, @RequestBody Article article){
        Article foundArticle = this.articleRepository.findById(id).orElse(null);
        if(foundArticle==null){
            return ResponseEntity.notFound().build();
        }
        foundArticle.setTitle(article.getTitle());
        foundArticle.setContent(article.getContent());
        foundArticle.setUpdatedAt(LocalDateTime.now());
        return ResponseEntity.status(HttpStatus.OK).body(articleRepository.save(foundArticle));
    }
    /**
     * DELETE ARTICLE
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Article> deleteArticle(@PathVariable Long id){
        Article foundArticle = this.articleRepository.findById(id).orElse(null);
        if(foundArticle == null){
            return ResponseEntity.notFound().build();
        }
        articleRepository.delete(foundArticle);
        return ResponseEntity.noContent().build();
    }
}
