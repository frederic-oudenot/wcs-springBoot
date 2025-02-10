package org.wildcodeschool.myblog.dto;
import org.wildcodeschool.myblog.model.Article;
import org.wildcodeschool.myblog.model.Author;

import java.util.List;

public class AuthorDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private List<Long> articleIds;

    // Getters et setters

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }


    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }


    public String getAuthor(){
            return this.firstName + " " + this.lastName;
    }
    public void setAuthor(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public List<Long> getArticleIds() {
        return articleIds;
    }
    public void setArticleIds(List<Long> articleIds) {
        this.articleIds = articleIds;
    }
}
