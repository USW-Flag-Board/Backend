package com.FlagHome.backend.domain.board.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
@AllArgsConstructor
public class BoardPostDto {
    private Long parentId;
    private String koreanName;
    private String englishName;
    private Long boardDepth;

}
