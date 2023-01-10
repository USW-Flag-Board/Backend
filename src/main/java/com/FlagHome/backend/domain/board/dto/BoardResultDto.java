package com.FlagHome.backend.domain.board.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class BoardResultDto {
    private String koreanName;
    private String englishName;
    private Long boardDepth;
    private List<BoardResultDto> children;
}
