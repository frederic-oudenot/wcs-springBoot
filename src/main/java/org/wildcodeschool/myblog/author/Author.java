package org.wildcodeschool.myblog.author;

import jakarta.persistence.*;
import org.wildcodeschool.myblog.articleAuthor.ArticleAuthor;

import java.util.List;

@Entity
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String lastName;

    @OneToMany(mappedBy = "author")
    private List<ArticleAuthor> articleAuthors;

    // Getters et setters


    public Long getId(){
        return id;
    }
    public void setId(Long id){
        this.id = id;
    }

    public String getAuthor(){
        return firstName + " " + lastName;
    }

    public String getFirstName(){
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setAuthor(String firstName, String lastName){
        this.firstName = firstName;
    }

    public List<ArticleAuthor> getArticleAuthors() {
        return articleAuthors;
    }
    public void setArticleAuthors(List<ArticleAuthor> articleAuthors) {
        this.articleAuthors = articleAuthors;
    }
}
