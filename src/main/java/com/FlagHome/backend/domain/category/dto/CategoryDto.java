package com.FlagHome.backend.domain.category.dto;


import com.FlagHome.backend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;

@Setter
@Getter
@AllArgsConstructor
public class CategoryDto {
    private Long id;
    private Long parentId;
    private String koreanName;
    private String englishName;
    private Long categoryDepth;

}
