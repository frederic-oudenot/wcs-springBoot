package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.Service.AuthorService;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.BadRequestException;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorRepository authorRepository;
    private final AuthorService authorService;

    public AuthorController(final AuthorRepository authorRepository, AuthorService authorService) {
        this.authorRepository = authorRepository;
        this.authorService = authorService;
    }

    @GetMapping()
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authorDTOs = authorService.getAllAuthors();
        if (authorDTOs.isEmpty()) {
            throw new ResourceNotFoundException("No authors found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(authorDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO foundAuthor = authorService.getAuthorById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foundAuthor);
    }

    @PostMapping()
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        if(author.getFirstName() != null && author.getLastName()!= null) {
            AuthorDTO createdAuthor = authorService.createAuthor(author);
            return ResponseEntity.status(HttpStatus.OK).body(createdAuthor);
        }
        throw new BadRequestException("Missing required fields firstName or lastName");

    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if(author.getFirstName()!=null && author.getLastName()!= null) {
            AuthorDTO updatedAuthor = authorService.updateAuthor(id, author);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
        }
        throw new BadRequestException("Missing required fields firstName or lastName");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.getAuthorById(id);
        authorService.deleteAuthor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
