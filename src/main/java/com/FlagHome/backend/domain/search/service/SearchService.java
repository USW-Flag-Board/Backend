package com.FlagHome.backend.domain.search.service;

import com.FlagHome.backend.domain.post.dto.PostDto;

import java.util.List;

public interface SearchService {
    List<PostDto> getAll();
    List<PostDto> getWithCategory(String categoryName);
    List<PostDto> getWithSearch(String searchCode, String SearchWord);
    List<PostDto> getWithCategoryAndSearch(String categoryName, String searchCode, String searchWord);
}
