package com.FlagHome.backend.domain.category.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.domain.category.dto.CategoryDto;
import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.dto.CategoryResultDto;
import com.FlagHome.backend.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public void createCategory (CategoryDto categoryDto) {

        Category parentCategory = null;

        if(categoryDto.getCategoryDepth()!=0){
            parentCategory = categoryRepository.findById(categoryDto.getParentId()).orElse(null);
            if(parentCategory == null)
                throw new CustomException(ErrorCode.CATEGORY_NOT_EXISTS);
        }

        Category category = Category.builder()
                .koreanName(categoryDto.getKoreanName())
                .englishName(categoryDto.getEnglishName())
                .parent(parentCategory)
                .categoryDepth(categoryDto.getCategoryDepth())
                .build();

        categoryRepository.save(category);

        if(parentCategory != null)
            parentCategory.getChildren().add(category);

    }

    @Transactional
    public void updateCategory (CategoryDto categoryDto) {

        Category category = categoryRepository.findById(categoryDto.getId()).orElse(null);
        if(category == null) {
            throw new CustomException(ErrorCode.CATEGORY_NOT_EXISTS);
        }

        Category parentCategory = null;
        if (categoryDto.getCategoryDepth() > 0){
            parentCategory = categoryRepository.findById(categoryDto.getParentId()).orElse(null);
        }
        category.setKoreanName(categoryDto.getKoreanName());
        category.setEnglishName(categoryDto.getEnglishName());
        category.setParent(parentCategory);
        category.setCategoryDepth(categoryDto.getCategoryDepth());

    }

    @Transactional
    public List<CategoryResultDto> getCategories () {

        List<CategoryResultDto> categories = categoryRepository
                .findAll().stream().map(CategoryResultDto::of)
                .collect(Collectors.toList());

        return categories;
    }

    @Transactional
    public void deleteCategory (long categoryId) {
        categoryRepository.deleteById(categoryId);
    }
}
