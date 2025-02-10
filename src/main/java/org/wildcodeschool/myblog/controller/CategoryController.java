package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.CategoryDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    public final CategoryRepository categoryRepository;

    public CategoryController(final CategoryRepository categoryRepository, final ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
    }

    /**
     * READ ALL CATEGORIES
     * */
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories found");
        }
        List<CategoryDTO> categoryDTOs = categories.stream().map(this::convertToDTO).collect(Collectors.toList());
        return ResponseEntity.ok(categoryDTOs);
    }
    /**
     * READ ONE CATEGORY
     * */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return ResponseEntity.ok(convertToDTO(foundCategory));
    }
    /**
     * POST ONE CATEGORY
     * */
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category){

        if(category.getName() != null){
            category.setCreatedAt(LocalDateTime.now());
            category.setUpdatedAt(LocalDateTime.now());
            Category createdCategory = categoryRepository.save(category);
            return ResponseEntity.ok(convertToDTO(createdCategory));
        }

        throw new ResourceNotFoundException("Category name is required");

    }

    /**
     * UPDATE ONE CATEGORY
     * */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category){

        if(category.getName() != null){
            Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found for id : " + id ));
            foundCategory.setUpdatedAt(LocalDateTime.now());
            foundCategory.setName(category.getName());
            Category updatedCategory = categoryRepository.save(foundCategory);
            return ResponseEntity.ok(convertToDTO(updatedCategory));
        }
        throw new ResourceNotFoundException("Category name is required");
    }

    /**
     * DELETE ONE CATEGORY
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        Category foundCategory = categoryRepository.findById(id).orElse(null);
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private CategoryDTO convertToDTO(Category category) {
        CategoryDTO categoryDTO = new CategoryDTO();
        categoryDTO.setId(category.getId());
        categoryDTO.setName(category.getName());
        if(category.getArticles() != null) {
            categoryDTO.setArticles(category.getArticles().stream().map(article -> {
                ArticleDTO articleDTO = new ArticleDTO();
                articleDTO.setId(article.getId());
                articleDTO.setTitle(article.getTitle());
                articleDTO.setContent(article.getContent());
                articleDTO.setUpdatedAt(article.getUpdatedAt());
                articleDTO.setCategoryName(article.getCategory().getName());
                return articleDTO;
            }).collect(Collectors.toList()));
        }
        return categoryDTO;
    }
}
