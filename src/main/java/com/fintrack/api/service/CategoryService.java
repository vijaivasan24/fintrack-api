package com.fintrack.api.service;

import com.fintrack.api.dto.CategoryRequest;
import com.fintrack.api.dto.CategoryResponse;
import com.fintrack.api.entity.Category;
import com.fintrack.api.entity.User;
import com.fintrack.api.repository.CategoryRepository;
import com.fintrack.api.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository, UserRepository userRepository) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public CategoryResponse create(CategoryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Category category = new Category();
        category.setName(request.getName());
        category.setDescription(request.getDescription());
        category.setUser(user);

        Category saved = categoryRepository.save(category);
        return toResponse(saved);
    }

    public List<CategoryResponse> getAllByUser(Long userId) {
        return categoryRepository.findByUserId(userId)
                .stream().map(this::toResponse).collect(Collectors.toList());
    }

    public CategoryResponse getById(Long id) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return toResponse(c);
    }

    public CategoryResponse update(Long id, CategoryRequest request) {
        Category c = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        c.setName(request.getName());
        c.setDescription(request.getDescription());
        // We typically don't change the user of a category, so we don't update user here

        return toResponse(categoryRepository.save(c));
    }

    public void delete(Long id) {
        categoryRepository.deleteById(id);
    }

    private CategoryResponse toResponse(Category c) {
        CategoryResponse response = new CategoryResponse();
        response.setId(c.getId());
        response.setName(c.getName());
        response.setDescription(c.getDescription());
        response.setUserId(c.getUser() != null ? c.getUser().getId() : null);
        return response;
    }
}
