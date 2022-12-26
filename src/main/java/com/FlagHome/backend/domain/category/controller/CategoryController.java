package com.FlagHome.backend.domain.category.controller;

import com.FlagHome.backend.domain.category.dto.CategoryDto;
import com.FlagHome.backend.domain.category.dto.CategoryResultDto;
import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);
        return new ResponseEntity(HttpStatus.CREATED); //BODY 던져주기!
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable long categoryId,
                                         @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        Category result =categoryService.updateCategory(categoryDto);
        return new ResponseEntity(HttpStatus.OK);
    }


    @GetMapping
    public ResponseEntity<?> getCategories() {
        return ResponseEntity.ok(categoryService.getCategories());
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity deleteCatgory(@PathVariable long categoryId) {
        categoryService.deleteCategory(categoryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }


}
