package org.wildcodeschool.myblog.Service;

import org.springframework.stereotype.Service;
import org.wildcodeschool.myblog.dto.CategoryDTO;
import org.wildcodeschool.myblog.exception.ResourceNotFoundException;
import org.wildcodeschool.myblog.mapper.CategoryMapper;
import org.wildcodeschool.myblog.model.Category;
import org.wildcodeschool.myblog.repository.CategoryRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryService(CategoryRepository categoryRepository, CategoryMapper categoryMapper) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        if(categories.isEmpty()){
            throw new ResourceNotFoundException("No categories found");
        }
        return categories.stream().map(categoryMapper::convertToDTO).collect(Collectors.toList());
    }

    public CategoryDTO getCategoryById(Long id) {
        Category category = categoryRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Category id " +id+" not found"));
        return categoryMapper.convertToDTO(category);
    }

    public CategoryDTO createCategory(Category category) {
        category.setCreatedAt(LocalDateTime.now());
        category.setUpdatedAt(LocalDateTime.now());
        Category createdCategory = categoryRepository.save(category);
        return categoryMapper.convertToDTO(createdCategory);
    }

    public CategoryDTO updateCategory(Long id, Category category) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found for id : " + id ));
        foundCategory.setUpdatedAt(LocalDateTime.now());
        foundCategory.setName(category.getName());
        Category updatedCategory = categoryRepository.save(foundCategory);
        return categoryMapper.convertToDTO(updatedCategory);
    }

    public void deleteCategory(Long id) {
        Category foundCategory = categoryRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Category not found for id : " + id ));
        categoryRepository.deleteById(id);

    }
}
