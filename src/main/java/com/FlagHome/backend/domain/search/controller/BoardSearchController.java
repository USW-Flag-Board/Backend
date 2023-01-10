package com.FlagHome.backend.domain.search.controller;

import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.search.service.impl.BoardSearchService;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/board/DeleteAPI") //테스트코드에서 충돌나서 변경해놨습니다 (23.01.10 강지은)
@RequiredArgsConstructor
public class BoardSearchController {
    private final BoardSearchService boardSearchService;

    @GetMapping
    public List<PostDto> getBoard(@RequestParam(value = "category", required = false) String categoryName,
                         @RequestParam(value = "search-code", required = false) String searchCode,
                         @RequestParam(value = "search-word", required = false) String searchWord) {
        if(categoryName != null && searchCode != null && searchWord != null)
            return boardSearchService.getWithCategoryAndSearch(categoryName, searchCode, searchWord);
        else if(categoryName != null && (searchCode == null || searchWord == null))
            return boardSearchService.getWithCategory(categoryName);
        else if(categoryName == null && (searchCode != null && searchWord != null))
            return boardSearchService.getWithSearch(searchCode, searchWord);
        else
            throw new CustomException(ErrorCode.BAD_REQUEST);
    }

    @GetMapping("/all")
    public List<PostDto> getAllBoard() {
        return boardSearchService.getAll();
    }
}
