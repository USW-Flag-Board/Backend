package com.FlagHome.backend.domain.post.repository;

import com.FlagHome.backend.domain.post.controller.dto.PostResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    /**
     * Version 1
     */
    /* List<LightPostDto> findMyPostList(String memberId);
    List<PostDto> findBoardWithCondition(String boardName, SearchType searchType, String searchWord);
    List<LightPostDto> findTopNPostListByDateAndLike(int postCount); */

    /**
     * Version 2
     */
    Page<PostResponse> getAllPostsByBoard(String boardName, Pageable pageable);
}
