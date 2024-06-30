package com.shopelec.backend.service.category;

import com.shopelec.backend.dto.request.CategoryRequest;
import com.shopelec.backend.dto.response.CategoryResponse;
import com.shopelec.backend.model.Category;
import com.shopelec.backend.repository.CategoryRepository;
import com.shopelec.backend.service.firebase.FirebaseStorageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE,makeFinal = true)
public class CategoryServiceImpl implements CategoryService{

    CategoryRepository categoryRepository;
    FirebaseStorageService firebaseStorageService;

    @Override
    public Category save(CategoryRequest request) throws IOException {
        if(request == null) {
            throw new NullPointerException("Category cannot be null");
        } else if(categoryRepository.existsByName(request.getName())) {
            throw new RuntimeException("Category already exists");
        }
        Category category = Category
                .builder()
                .name(request.getName())
                .build();
        return categoryRepository.save(category);
    }

    @Override
    public Category update(CategoryRequest request) throws IOException {
        if(request == null) {
            throw new NullPointerException("Category cannot be null");
        }
        Category category = Category
                .builder()
                .id(request.getId())
                .name(request.getName())
                .build();
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
    public List<CategoryResponse> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        List<CategoryResponse> responses = new ArrayList<>();
        for(Category category : categories) {
            responses.add(CategoryResponse
                    .builder()
                    .id(category.getId())
                    .name(category.getName())
                    .build()
            );
        }
        return responses;
    }

    @Override
    public boolean existByName(String name) {
        return categoryRepository.existsByName(name);
    }

    @Override
    public List<Category> getAll() {
        return categoryRepository.findAll();
    }


}
