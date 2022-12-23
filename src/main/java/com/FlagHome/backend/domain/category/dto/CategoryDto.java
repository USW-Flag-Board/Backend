package com.FlagHome.backend.domain.category.dto;


import com.FlagHome.backend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@NoArgsConstructor  //테스트용
@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private Long parentId;
    private String koreanName;
    private String englishName;
    private Long categoryDepth;

    /*
    public CategoryDto(Category category){
        this.id = category.getId();
        this.parentId = category.getParent().getId();
        this.koreanName = category.getKoreanName();
        this.categoryDepth = category.getCategoryDepth();
    }

     */
}
