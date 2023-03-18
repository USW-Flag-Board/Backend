package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.board.enums.SearchType;
import com.FlagHome.backend.domain.post.controller.dto.LightPostDto;
import com.FlagHome.backend.domain.post.controller.dto.PostDto;

import java.util.List;

public interface PostRepositoryCustom {

    List<LightPostDto> findMyPostList(String memberId);
    List<PostDto> findBoardWithCondition(String boardName, SearchType searchType, String searchWord);
    List<LightPostDto> findTopNPostListByDateAndLike(int postCount);
}
