package com.shopelec.backend.service.category;

import com.shopelec.backend.model.Category;
import com.shopelec.backend.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;

    @Override
    public Category save(Category category) {
        if(category == null) {
            throw new NullPointerException("Category cannot be null");
        } else if(categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category already exists");
        }
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        if(category == null) {
            throw new NullPointerException("Category cannot be null");
        } else if(!categoryRepository.existsByName(category.getName())) {
            throw new RuntimeException("Category not found");
        }
        return categoryRepository.save(category);
    }

    @Override
    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }
}
