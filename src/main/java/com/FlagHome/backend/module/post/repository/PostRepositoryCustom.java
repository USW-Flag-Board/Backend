package com.FlagHome.backend.module.post.repository;

import com.FlagHome.backend.module.post.controller.dto.response.PostResponse;
import com.FlagHome.backend.module.post.controller.dto.response.SearchResponse;
import com.FlagHome.backend.module.post.entity.enums.SearchOption;
import com.FlagHome.backend.module.post.entity.enums.SearchPeriod;
import com.FlagHome.backend.module.post.entity.enums.TopPostCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

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

    List<PostResponse> getAllPostsByLoginId(String loginId);

    List<PostResponse> getTopFiveByCondition(TopPostCondition condition);

    SearchResponse integrationSearch(String keyword);

    SearchResponse searchWithCondition(String boardName, String keyword, SearchPeriod period, SearchOption option);
}