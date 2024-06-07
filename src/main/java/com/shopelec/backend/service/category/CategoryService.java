package com.shopelec.backend.service.category;

import com.shopelec.backend.dto.request.CategoryRequest;
import com.shopelec.backend.dto.response.CategoryResponse;
import com.shopelec.backend.model.Category;

import java.io.IOException;
import java.util.List;

public interface CategoryService {
    Category save(CategoryRequest request) throws IOException;
    Category update(CategoryRequest request) throws IOException;
    boolean delete(Long id);
    List<CategoryResponse> getAllCategory();
    boolean existByName(String name);
}
