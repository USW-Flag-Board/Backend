package com.FlagHome.backend.domain.category.mapper;

import  com.FlagHome.backend.domain.category.dto.CategoryDto;
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
    default Category CategoryDtoToCategory(CategoryDto categoryDto, CategoryService categoryService) {

        if ( categoryDto == null ) {
            return null;
        }

        Category category = new Category();
        Category parentCategory = null;

        //patch일 경우 변경되는 데이터를 제외하고는 null이 들어와서 체크가 필요한데, 코드가 더러운것 같아서 고민하고 있습니다. (22.12.31 강지은)
        if(categoryDto.getCategoryDepth() == null) {}
        else {
            if(categoryDto.getCategoryDepth() !=0){
                parentCategory = categoryService.findVerifiedCategory( categoryDto.getParentId());
            }
        }

        category.setId( categoryDto.getId() );
        category.setKoreanName( categoryDto.getKoreanName() );
        category.setEnglishName( categoryDto.getEnglishName() );
        category.setCategoryDepth( categoryDto.getCategoryDepth() );
        category.setParent(parentCategory);

        return category;
    }
}
