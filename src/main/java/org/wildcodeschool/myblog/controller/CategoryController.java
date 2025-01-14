package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.ArticleRepository;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/categories")
public class CategoryController {
    public final CategoryRepository categoryRepository;
    public final ArticleRepository articleRepository;
    public CategoryController(final CategoryRepository categoryRepository, final ArticleRepository articleRepository) {
        this.categoryRepository = categoryRepository;
        this.articleRepository = articleRepository;
    }

    /**
     * READ ALL CATEGORIES
     * */
    @GetMapping()
    public ResponseEntity<List<Category>> getAllCategories(){
        try{
            List<Category> categories = categoryRepository.findAll();
            if(categories.isEmpty()){
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(categories);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * READ ONE CATEGORY
     * */
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategoryById(@PathVariable Long id){
        try {
            Category foundCategory = categoryRepository.findById(id).orElse(null);;
            if (foundCategory == null) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(foundCategory);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    /**
     * POST ONE CATEGORY
     * */
    @PostMapping()
    public ResponseEntity<Category> createCategory(@RequestBody Category category){
        try {
            category.setCreatedAt(LocalDateTime.now());
            category.setUpdatedAt(LocalDateTime.now());
        Category createdCategory = categoryRepository.save(category);
        if (createdCategory == null) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(createdCategory);
        }catch (Exception e){
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * UPDATE ONE CATEGORY
     * */
    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable Long id, @RequestBody Category category){
        try {
            Category foundCategory = categoryRepository.findById(id).orElse(null);
            foundCategory.setUpdatedAt(LocalDateTime.now());
            foundCategory.setName(category.getName());
            Category updatedCategory = categoryRepository.save(foundCategory);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            System.out.println(e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE ONE CATEGORY
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable Long id){
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
}
