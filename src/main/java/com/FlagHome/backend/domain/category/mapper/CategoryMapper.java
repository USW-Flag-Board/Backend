package com.FlagHome.backend.domain.category.mapper;

import com.FlagHome.backend.domain.category.dto.CategoryPatchDto;
import com.FlagHome.backend.domain.category.dto.CategoryPostDto;
import com.FlagHome.backend.domain.category.dto.CategoryResultDto;
import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.service.CategoryService;
import org.mapstruct.Mapper;

import javax.transaction.Transactional;
import java.util.List;

@Mapper(componentModel = "spring")
public interface CategoryMapper {

    CategoryResultDto CategoryToCategoryResultDto(Category category);
    List<CategoryResultDto> CategoryListToCategoryResultDtoList(List<Category> categories);

    @Transactional
    default Category CategoryPostDtoToCategory(CategoryPostDto categoryPostDto, CategoryService categoryService) {

        if ( categoryPostDto == null ) {
            return null;
        }

        Category parentCategory = null;


        if(categoryPostDto.getCategoryDepth() !=0){
                parentCategory = categoryService.findVerifiedCategory( categoryPostDto.getParentId());
        }


        Category category = Category.builder()
                .koreanName(categoryPostDto.getKoreanName())
                .englishName(categoryPostDto.getEnglishName())
                .categoryDepth(categoryPostDto.getCategoryDepth())
                .parent(parentCategory)
                .build();

        return category;
    }



    default Category CategoryPatchDtoToCategory(CategoryPatchDto categoryPatchDto, CategoryService categoryService) {
        if (categoryPatchDto == null) {
            return null;
        }

        Category parentCategory = null;
        if(categoryPatchDto.getCategoryDepth() != null){ //수정이므로, depth는 Null일 수 있음
            if(categoryPatchDto.getCategoryDepth() !=0){ //depth가 0이면 부모가 없음 > 0이 아니면 부모의 정보를 가져옴
                parentCategory = categoryService.findVerifiedCategory(categoryPatchDto.getParentId());
            }
        }

        Category category = Category.builder()
                .koreanName(categoryPatchDto.getKoreanName())
                .englishName(categoryPatchDto.getEnglishName())
                .categoryDepth(categoryPatchDto.getCategoryDepth())
                .parent(parentCategory)
                .build();

        category.setId(categoryPatchDto.getId());

        return category;
    }
}
