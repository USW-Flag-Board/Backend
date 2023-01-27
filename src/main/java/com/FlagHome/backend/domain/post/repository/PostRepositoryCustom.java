package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.board.enums.SearchType;
import com.FlagHome.backend.domain.post.dto.PostDto;

import java.util.List;

public interface PostRepositoryCustom {
    List<PostDto> findBoardWithCondition(String boardName, SearchType searchType, String searchWord);
    List<PostDto> findTop3PostListByDateAndLike();
}
