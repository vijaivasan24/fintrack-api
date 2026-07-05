package com.fintrack.api.controller;

import com.fintrack.api.dto.CategoryRequest;
import com.fintrack.api.dto.CategoryResponse;
import com.fintrack.api.service.CategoryService;
import com.fintrack.api.security.CustomUserDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity<CategoryResponse> create(@RequestBody CategoryRequest request, @AuthenticationPrincipal CustomUserDetails userDetails) {
        request.setUserId(userDetails.getUser().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryService.create(request));
    }

    @GetMapping
    public ResponseEntity<List<CategoryResponse>> getAll(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(categoryService.getAllByUser(userDetails.getUser().getId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryResponse> getById(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(categoryService.getById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryResponse> update(@PathVariable Long id,
                                                   @RequestBody CategoryRequest request,
                                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        request.setUserId(userDetails.getUser().getId());
        return ResponseEntity.ok(categoryService.update(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        categoryService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
