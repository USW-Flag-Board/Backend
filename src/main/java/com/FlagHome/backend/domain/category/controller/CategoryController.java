package com.FlagHome.backend.domain.category.controller;

import com.FlagHome.backend.domain.category.dto.CategoryDto;
import com.FlagHome.backend.domain.category.mapper.CategoryMapper;
import com.FlagHome.backend.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.createCategory(mapper.CategoryDtoToCategory(categoryDto,categoryService));
        return new ResponseEntity(HttpStatus.CREATED);
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
    public ResponseEntity deleteCatgory(@PathVariable("category-id") long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
