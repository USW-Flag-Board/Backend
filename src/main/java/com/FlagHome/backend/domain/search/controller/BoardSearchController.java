package com.FlagHome.backend.domain.search.controller;

import com.FlagHome.backend.domain.search.service.impl.BoardSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/board")
@RequiredArgsConstructor
public class BoardSearchController {
    private final BoardSearchService boardSearchService;

    @GetMapping
    public void getBoard(@RequestParam(value = "category") String categoryName,
                         @RequestParam(value = "searchCode", required = false) String searchCode,
                         @RequestParam(value = "searchWord", required = false) String searchWord) {
        
    }
}
