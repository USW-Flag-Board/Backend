package com.FlagHome.backend.domain.board.dto;


import lombok.*;

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
