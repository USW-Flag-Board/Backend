package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.search.enums.SearchType;
import com.FlagHome.backend.domain.post.dto.PostDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostDto> findBoardWithCondition(String categoryName, SearchType searchType, String searchWord);
}
