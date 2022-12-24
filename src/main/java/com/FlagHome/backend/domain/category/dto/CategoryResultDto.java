package com.FlagHome.backend.domain.category.dto;


import com.FlagHome.backend.domain.category.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategoryResultDto {
    private Long id;
    private String koreanName;
    private String englishName;
    private Long depth;
    private List<CategoryResultDto> children;

    public static CategoryResultDto of(Category category) {
        return new CategoryResultDto(
                category.getId(),
                category.getKoreanName(),
                category.getEnglishName(),
                category.getCategoryDepth(),
                category.getChildren().stream().map(CategoryResultDto::of).collect(Collectors.toList())
        );
    }
}
