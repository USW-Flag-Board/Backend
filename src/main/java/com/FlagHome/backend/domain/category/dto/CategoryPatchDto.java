package com.FlagHome.backend.domain.category.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class CategoryPatchDto {

    private Long id; //이 필드는 patch시에 필요하지만, CategoryDto 자체는 push/patch에 둘다 사용하고 있음,이로인해 null 처리가 따로 필요해짐 따라서 분리를 해줄 필요성이 보인다. (23.01.06 강지은)
    //dto 분리로 인한 리팩토링 작업 완료 (23.01.08 강지은)
    private Long parentId;
    private String koreanName;
    private String englishName;
    private Long categoryDepth;
}
