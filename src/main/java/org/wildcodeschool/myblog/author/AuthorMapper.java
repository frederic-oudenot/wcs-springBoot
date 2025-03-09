package org.wildcodeschool.myblog.author;

import org.springframework.stereotype.Component;
import org.wildcodeschool.myblog.articleAuthor.ArticleAuthor;

import java.util.stream.Collectors;

@Component
public class AuthorMapper {
    public AuthorDTO convertToAuthorDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstName(author.getFirstName());
        authorDTO.setLastName(author.getLastName());
        if(author.getArticleAuthors() != null) {
            authorDTO.setArticleIds(author.getArticleAuthors().stream().map(ArticleAuthor::getId).collect(Collectors.toList()));
        }
        return authorDTO;
    }


}
