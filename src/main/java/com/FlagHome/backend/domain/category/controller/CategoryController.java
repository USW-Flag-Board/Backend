package com.FlagHome.backend.domain.category.controller;

import com.FlagHome.backend.domain.category.dto.CategoryPatchDto;
import com.FlagHome.backend.domain.category.dto.CategoryPostDto;
import com.FlagHome.backend.domain.category.dto.CategoryResultDto;
import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.mapper.CategoryMapper;
import com.FlagHome.backend.domain.category.service.CategoryService;
import com.FlagHome.backend.global.util.UriCreator;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper mapper;
    private final static String CATEGORY_DEFAULT_URL = "/api/categories";

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryPostDto categoryPostDto) {
        Category resultCategory = categoryService.createCategory(mapper.CategoryPostDtoToCategory(categoryPostDto,categoryService));
        URI location = UriCreator.createUri(CATEGORY_DEFAULT_URL, resultCategory.getId());

        return ResponseEntity.created(location).build();
    }

    @PatchMapping("/{category-id}")
    public ResponseEntity updateCategory(@PathVariable("category-id") long categoryId,
                                         @RequestBody CategoryPatchDto categoryPatchDto) {
        categoryPatchDto.setId(categoryId);
        Category category = mapper.CategoryPatchDtoToCategory(categoryPatchDto, categoryService);
        categoryService.updateCategory(category);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    public ResponseEntity<?> getCategories() {
        List<CategoryResultDto> result =  mapper.CategoryListToCategoryResultDtoList(categoryService.getCategories());
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{category-id}")
    public ResponseEntity deleteCategory(@PathVariable("category-id") long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.noContent().build();
    }
}
