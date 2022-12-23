package com.FlagHome.backend.domain.category.dto;


import com.FlagHome.backend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@NoArgsConstructor  //테스트용
@AllArgsConstructor
@Getter
public class CategoryDto {
    private Long id;
    private Long parentId;
    private String name;
    private Long categoryDepth;

    public CategoryDto(Category category){
        this.id = category.getId();
        this.parentId = category.getParent().getId();
        this.name = category.getName();
        this.categoryDepth = category.getCategoryDepth();
    }
}
