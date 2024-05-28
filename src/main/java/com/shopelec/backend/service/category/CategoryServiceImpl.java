package com.shopelec.backend.service.category;

import com.shopelec.backend.model.Category;
import com.shopelec.backend.repository.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

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
        }
        return categoryRepository.save(category);
    }

    @Override
    public boolean delete(Long id) {
        if(categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Override
    public List<Category> getAllCategory() {
        return categoryRepository.findAll();
    }

    @Override
    public boolean existByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public boolean existById(Long id) {
        return categoryRepository.existsById(id);
    }
}
