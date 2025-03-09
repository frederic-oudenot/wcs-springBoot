package org.wildcodeschool.myblog.article;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    List<Article> findByTitle(String title);
    List<Article> findByContentContaining(String content);
    List<Article> findByCreatedAtAfter(LocalDateTime dateTime);
    List<Article> findTop5ByOrderByCreatedAtDesc();
}