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
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    @Transactional
    public void createCategory (CategoryDto categoryDto) {

        Category parentCategory = null;

        if(categoryDto.getCategoryDepth()!=0){
            parentCategory = findVerifiedCategory(categoryDto.getParentId());
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
    public Category updateCategory (CategoryDto categoryDto) {

        Category category = findVerifiedCategory(categoryDto.getId());


        Optional.ofNullable(categoryDto.getKoreanName())
                .ifPresent(koreanName -> category.setKoreanName(koreanName));
        Optional.ofNullable(categoryDto.getEnglishName())
                .ifPresent(englishName -> category.setEnglishName(englishName));
        Optional.ofNullable(categoryDto.getCategoryDepth())
                .ifPresent(depth -> category.setCategoryDepth(depth));
        Optional.ofNullable(categoryDto.getParentId())
                .ifPresent(parent -> category.setParent(
                        findVerifiedCategory(categoryDto.getParentId())
                ));

        return categoryRepository.save(category);

    }

    @Transactional
    public List<CategoryResultDto> getCategories () {

        List<CategoryResultDto> categories = categoryRepository
                .findAll().stream()
                .map(CategoryResultDto::of)
                .collect(Collectors.toList());

        return categories;
    }

    @Transactional
    public void deleteCategory (long categoryId) {
        Category category = findVerifiedCategory(categoryId);
        categoryRepository.delete(category);
    }

    private Category findVerifiedCategory(long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category findCategory = optionalCategory.orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_EXISTS));
        return findCategory;
    }
}
