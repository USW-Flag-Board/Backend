package com.FlagHome.backend.domain.category.service;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
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
    public Category createCategory(Category category){

        categoryRepository.save(category);
        Category parentCategory = category.getParent();

        if(parentCategory != null)
            parentCategory.getChildren().add(category);

        return category;
    }
    @Transactional
    public Category updateCategory (Category category) {

        Category findCategory = findVerifiedCategory(category.getId());

        Optional.ofNullable(category.getKoreanName())
                .ifPresent(koreanName -> findCategory.setKoreanName(koreanName));
        Optional.ofNullable(category.getEnglishName())
                .ifPresent(englishName -> findCategory.setEnglishName(englishName));
        Optional.ofNullable(category.getCategoryDepth())
                .ifPresent(depth -> findCategory.setCategoryDepth(depth));
        Optional.ofNullable(category.getParent())
                .ifPresent(parent -> findCategory.setParent(
                        findVerifiedCategory(category.getParent().getId())
                ));

        return categoryRepository.save(findCategory);

    }

    @Transactional
    public List<Category> getCategories () {
        return categoryRepository.findAll();
    }

    @Transactional
    public void deleteCategory (long categoryId) {
        Category category = findVerifiedCategory(categoryId);
        categoryRepository.delete(category);
    }

    public Category findVerifiedCategory(long categoryId) {
        Optional<Category> optionalCategory = categoryRepository.findById(categoryId);
        Category findCategory = optionalCategory.orElseThrow(()-> new CustomException(ErrorCode.CATEGORY_NOT_EXISTS));
        return findCategory;
    }
}
