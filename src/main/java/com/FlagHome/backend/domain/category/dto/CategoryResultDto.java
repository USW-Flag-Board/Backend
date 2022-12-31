package com.FlagHome.backend.domain.category.dto;


import com.FlagHome.backend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class CategoryResultDto {
    private Long id;
    private String koreanName;
    private String englishName;
    private Long categoryDepth;
    private List<CategoryResultDto> children;

}
