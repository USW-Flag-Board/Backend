package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.controller.dto.response.GetPostResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostResponse;
import com.Flaground.backend.module.post.controller.dto.response.SearchResponse;
import com.Flaground.backend.module.post.domain.enums.SearchOption;
import com.Flaground.backend.module.post.domain.enums.SearchPeriod;
import com.Flaground.backend.module.post.domain.enums.TopPostCondition;
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
    GetPostResponse getWithReplies(Long memberId, Long postId);

    Page<PostResponse> getPostsOfBoard(String boardName, Pageable pageable);

    List<PostResponse> getPostsByLoginId(String loginId);

    List<PostResponse> getTopFiveByCondition(TopPostCondition condition);

    SearchResponse integrationSearch(String keyword);

    SearchResponse searchWithCondition(String boardName, String keyword, SearchPeriod period, SearchOption option);
}
