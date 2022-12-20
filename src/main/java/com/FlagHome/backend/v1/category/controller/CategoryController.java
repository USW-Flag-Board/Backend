package com.FlagHome.backend.v1.category.controller;

import com.FlagHome.backend.v1.category.dto.CategoryDto;
import com.FlagHome.backend.v1.category.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @Autowired
    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @PostMapping
    public ResponseEntity createCategory(@RequestBody CategoryDto categoryDto) {
        categoryService.createCategory(categoryDto);
        return new ResponseEntity(HttpStatus.CREATED); //BODY 던져주기!
    }

    @PutMapping("/{categoryId}")
    public ResponseEntity updateCategory(@PathVariable long categoryId,
                                         @RequestBody CategoryDto categoryDto) {
        categoryDto.setId(categoryId);
        categoryService.updateCategory(categoryDto);
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
