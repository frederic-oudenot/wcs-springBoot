package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.AuthorDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.AuthorMapper;
import org.wildcodeschool.myblog.model.Author;
import org.wildcodeschool.myblog.repository.ArticleAuthorRepository;
import org.wildcodeschool.myblog.repository.AuthorRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final AuthorMapper authorMapper;
    private final ArticleAuthorRepository articleAuthorRepository;

    public AuthorService(AuthorRepository authorRepository, AuthorMapper authorMapper, ArticleAuthorRepository articleAuthorRepository) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
        this.articleAuthorRepository = articleAuthorRepository;
    }

    public List<AuthorDTO> getAllAuthors() {
        List<Author> authorDTOs = authorRepository.findAll();
        if (authorDTOs.isEmpty()) {
            throw new ResourceNotFoundException("No authors found");
        }
        return authorDTOs.stream().map(authorMapper::convertToAuthorDTO).collect(Collectors.toList());
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
        Author foundAuthor = authorRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("No author found with id " + id));
        articleAuthorRepository.deleteAll(foundAuthor.getArticleAuthors());
        authorRepository.delete(foundAuthor);
    }
}


