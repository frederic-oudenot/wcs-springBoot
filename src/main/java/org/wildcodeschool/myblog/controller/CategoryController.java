package org.wildcodeschool.myblog.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wildcodeschool.myblog.Service.CategoryService;
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
    private final CategoryService categoryService;

    public CategoryController(final CategoryRepository categoryRepository, final ArticleRepository articleRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    /**
     * READ ALL CATEGORIES
     * */
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories(){
        List<CategoryDTO> categoriesDTO = categoryService.getAllCategories();
        if(categoriesDTO.isEmpty()){
            throw new ResourceNotFoundException("No categories found");
        }
        return ResponseEntity.status(HttpStatus.OK).body(categoriesDTO);
    }
    /**
     * READ ONE CATEGORY
     * */
    @GetMapping("/{id}")
    public ResponseEntity<CategoryDTO> getCategoryById(@PathVariable Long id){
        CategoryDTO foundCategory = categoryService.getCategoryById(id);
        return ResponseEntity.ok(foundCategory);
    }
    /**
     * POST ONE CATEGORY
     * */
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody Category category){
        if(category.getName() != null){
            CategoryDTO createdCategory = categoryService.createCategory(category);
            return ResponseEntity.ok(createdCategory);
        }
        throw new ResourceNotFoundException("Category name is required");
    }

    /**
     * UPDATE ONE CATEGORY
     * */
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody Category category){

        if(category.getName() != null){
            CategoryDTO updatedCategory = categoryService.updateCategory(id, category);
            return ResponseEntity.ok(updatedCategory);
        }
        throw new ResourceNotFoundException("Category name is required");
    }

    /**
     * DELETE ONE CATEGORY
     * */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }


}
