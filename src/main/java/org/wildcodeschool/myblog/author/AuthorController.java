package org.wildcodeschool.myblog.author;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.exception.BadRequestException;

import java.util.List;

@RestController
@RequestMapping("/authors")
public class AuthorController {
    private final AuthorService authorService;

    public AuthorController( AuthorService authorService) {
        this.authorService = authorService;
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<List<AuthorDTO>> getAllAuthors() {
        List<AuthorDTO> authorDTOs = authorService.getAllAuthors();
        return ResponseEntity.status(HttpStatus.OK).body(authorDTOs);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        AuthorDTO foundAuthor = authorService.getAuthorById(id);
        return ResponseEntity.status(HttpStatus.OK).body(foundAuthor);
    }

    @PostMapping()
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        if(author.getFirstName() != null && author.getLastName()!= null) {
            AuthorDTO createdAuthor = authorService.createAuthor(author);
            return ResponseEntity.status(HttpStatus.OK).body(createdAuthor);
        }
        throw new BadRequestException("Missing required fields firstName or lastName");

    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if(author.getFirstName()!=null && author.getLastName()!= null) {
            AuthorDTO updatedAuthor = authorService.updateAuthor(id, author);
            return ResponseEntity.status(HttpStatus.OK).body(updatedAuthor);
        }
        throw new BadRequestException("Missing required fields firstName or lastName");
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
