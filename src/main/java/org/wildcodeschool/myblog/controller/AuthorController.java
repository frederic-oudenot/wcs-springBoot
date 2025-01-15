package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.model.ArticleAuthor;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;
    public AuthorController(final AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }

    private AuthorDTO convertToAuthorDTO(Author author) {
        AuthorDTO authorDTO = new AuthorDTO();
        authorDTO.setId(author.getId());
        authorDTO.setFirstName(author.getFirstName());
        authorDTO.setLastName(author.getLastName());
        if(author.getArticleAuthors() != null) {
            authorDTO.setArticleIds(author.getArticleAuthors().stream().map(ArticleAuthor::getId).collect(Collectors.toList()));
        }
        return authorDTO;
    }


    @GetMapping()
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        try {
            List<Author> foundAuthors = authorRepository.findAll();
            if (foundAuthors.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
            }
            List<AuthorDTO> authorDTOs = foundAuthors.stream().map(this::convertToAuthorDTO).collect(Collectors.toList());
            return ResponseEntity.status(HttpStatus.OK).body(authorDTOs);
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        try{
            Author foundAuthor = authorRepository.findById(id).orElse(null);
            if (foundAuthor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            return ResponseEntity.status(HttpStatus.OK).body(convertToAuthorDTO(foundAuthor));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping()
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        try{
            Author createdAuthor = authorRepository.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToAuthorDTO(createdAuthor));
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        Author foundAuthor = authorRepository.findById(id).orElse(null);
        if (foundAuthor == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        foundAuthor.setId(id);
        foundAuthor.setAuthor(author.getFirstName(), author.getLastName());
        Author updatedAuthor = authorRepository.save(foundAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(convertToAuthorDTO(updatedAuthor));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        try {
            Author foundAuthor = authorRepository.findById(id).orElse(null);
            if (foundAuthor == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }
            authorRepository.delete(foundAuthor);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
