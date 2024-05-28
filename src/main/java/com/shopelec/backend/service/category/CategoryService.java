package com.shopelec.backend.service.category;

import com.shopelec.backend.model.Category;

import java.util.List;

public interface CategoryService {
    Category save(Category category);
    Category update(Category category);
    boolean delete(Long id);
    List<Category> getAllCategory();
    boolean existByName(String name);
    boolean existById(Long id);
}
