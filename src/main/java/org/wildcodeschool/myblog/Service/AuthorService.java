package org.wildcodeschool.myblog.Service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> foundAuthors = authorRepository.findAll();
        return foundAuthors.stream().map(authorMapper::convertToAuthorDTO).collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(Long id) throws ResourceNotFoundException {
        Author foundAuthor = authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No author found with id " + id));
        return authorMapper.convertToAuthorDTO(foundAuthor);
    }

    public AuthorDTO createAuthor(Author author) {
        Author createdAuthor = authorRepository.save(author);
        return authorMapper.convertToAuthorDTO(createdAuthor);
    }

    public AuthorDTO updateAuthor(Long id, Author author) {
        Author foundAuthor = authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No author found with id " + id));
        foundAuthor.setId(id);
        foundAuthor.setAuthor(author.getFirstName(), author.getLastName());
        Author updatedAuthor = authorRepository.save(foundAuthor);
        return authorMapper.convertToAuthorDTO(updatedAuthor);
    }
    public void deleteAuthor(Long id) {
        authorRepository.deleteById(id);
    }
}


