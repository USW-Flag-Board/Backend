package com.FlagHome.backend.domain.category.dto;


import lombok.*;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CategoryPostDto {
    private Long parentId;
    private String koreanName;
    private String englishName;
    private Long categoryDepth;

}
