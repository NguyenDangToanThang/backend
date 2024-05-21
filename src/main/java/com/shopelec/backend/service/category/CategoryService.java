package com.shopelec.backend.service.category;

import com.shopelec.backend.model.Category;

public interface CategoryService {
    Category save(Category category);
    Category update(Category category);
    void delete(Long id);
}
