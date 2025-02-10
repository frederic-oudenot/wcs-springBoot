package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.BadRequestException;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
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
        List<Author> foundAuthors = authorRepository.findAll();
        if (foundAuthors.isEmpty()) {
            throw new ResourceNotFoundException("No authors found");
        }
        List<AuthorDTO> authorDTOs = foundAuthors.stream().map(this::convertToAuthorDTO).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(authorDTOs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable Long id) {
        Author foundAuthor = authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No author found with id " + id));
        return ResponseEntity.status(HttpStatus.OK).body(convertToAuthorDTO(foundAuthor));
    }

    @PostMapping()
    public ResponseEntity<AuthorDTO> createAuthor(@RequestBody Author author) {
        if(author.getFirstName()!=null && author.getLastName()!= null) {
            Author createdAuthor = authorRepository.save(author);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToAuthorDTO(createdAuthor));
        }
        throw new BadRequestException("Missing required fields firstName or lastName");

    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable Long id, @RequestBody Author author) {
        if(author.getFirstName()!=null && author.getLastName()!= null) {
        Author foundAuthor = authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No author found with id " + id));

        foundAuthor.setId(id);
        foundAuthor.setAuthor(author.getFirstName(), author.getLastName());
        Author updatedAuthor = authorRepository.save(foundAuthor);
        return ResponseEntity.status(HttpStatus.OK).body(convertToAuthorDTO(updatedAuthor));
        }
        throw new BadRequestException("Missing required fields firstName or lastName");

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        Author foundAuthor = authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No author found with id " + id));
        authorRepository.delete(foundAuthor);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
