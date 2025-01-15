package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.dto.ArticleDTO;
import org.wildcodeschool.myblog.dto.CategoryDTO;
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
        try{
            List<Category> categories = categoryRepository.findAll();
            if(categories.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            List<CategoryDTO> categoryDTOs = categories.stream().map(this::convertToDTO).collect(Collectors.toList());
            return ResponseEntity.ok(categoryDTOs);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * READ ONE CATEGORY
     * */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        try {
            Category foundCategory = categoryRepository.findById(id).orElse(null);;
            if (foundCategory == null) {
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(convertToDTO(foundCategory));
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * POST ONE CATEGORY
     * */
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category){
        try {
            category.setCreatedAt(LocalDateTime.now());
            category.setUpdatedAt(LocalDateTime.now());
        Category createdCategory = categoryRepository.save(category);
        if (createdCategory == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(convertToDTO(createdCategory));
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * UPDATE ONE CATEGORY
     * */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
            Category foundCategory = categoryRepository.findById(id).orElse(null);
            foundCategory.setUpdatedAt(LocalDateTime.now());
            foundCategory.setName(category.getName());
            Category updatedCategory = categoryRepository.save(foundCategory);
            return ResponseEntity.ok(convertToDTO(updatedCategory));
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE ONE CATEGORY
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
    try{
        Category foundCategory = categoryRepository.findById(id).orElse(null);
        if (foundCategory == null) {
            return ResponseEntity.noContent().build();
        }
        categoryRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }catch (Exception e) {
        System.out.println(e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }
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
