package com.FlagHome.backend.domain.category.controller;

import com.FlagHome.backend.domain.category.dto.CategoryDto;
import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.mapper.CategoryMapper;
import com.FlagHome.backend.domain.category.service.CategoryService;
import com.FlagHome.backend.global.utility.URICreator;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;
    private final static String CATEGORY_DEFAULT_URL = "/api/categories";

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryDto categoryDto) {
        Category resultCategory = categoryService.createCategory(mapper.CategoryDtoToCategory(categoryDto,categoryService));
        URI location = URICreator.createUri(CATEGORY_DEFAULT_URL, resultCategory.getId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{category-id}")
    public ResponseEntity updateCategory(@PathVariable("category-id") long categoryId,
                                         @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        categoryService.updateCategory(mapper.CategoryDtoToCategory(categoryDto,categoryService));
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(mapper.CategoryListToCategoryResultDtoList(categoryService.getCategories()));
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity deleteCategory(@PathVariable("category-id") long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
