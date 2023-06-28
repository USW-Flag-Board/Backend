package com.Flaground.backend.module.post.controller;

import com.Flaground.backend.global.annotation.EnumFormat;
import com.Flaground.backend.global.common.response.ApplicationResponse;
import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.global.utility.SecurityUtils;
import com.Flaground.backend.global.utility.UriCreator;
import com.Flaground.backend.module.post.controller.dto.request.CreateReplyRequest;
import com.Flaground.backend.module.post.controller.dto.request.PostRequest;
import com.Flaground.backend.module.post.controller.dto.request.SearchRequest;
import com.Flaground.backend.module.post.controller.dto.request.UpdateReplyRequest;
import com.Flaground.backend.module.post.controller.dto.response.GetPostResponse;
import com.Flaground.backend.module.post.controller.dto.response.LikeResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostResponse;
import com.Flaground.backend.module.post.controller.mapper.PostMapper;
import com.Flaground.backend.module.post.domain.enums.TopPostCondition;
import com.Flaground.backend.module.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "post", description = "게시글 API")
@Validated
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private static final String BASE_URL = "/posts";
    private final PostService postService;
    private final PostMapper postMapper;

    @Tag(name = "post")
    @Operation(summary = "게시글 상세보기", description = "토큰을 넣으면 회원이 좋아요한 게시글들을 확인할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "상세보기 성공"),
            @ApiResponse(responseCode = "400", description = "접근할 수 없는 게시글, 리다이렉트 해줄 것"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물입니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public ApplicationResponse<GetPostResponse> getPost(@PathVariable Long id) {
        GetPostResponse response = postService.get(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 모두 가져오기", description = "게시판 이름에 맞춰서 페이징 처리<br> page만 요청하면 된다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판의 모든 게시글 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판입니다.")
    })
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<Page<PostResponse>> getPostsOfBoard(@RequestParam("board") @NotBlank String boardName,
                                                                   @PageableDefault Pageable pageable) {
        Page<PostResponse> response = postService.getPostsOfBoard(boardName, pageable);
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "post")
    @Operation(summary = "멤버 페이지 정보 가져오기 (게시글)", description = "멤버 페이지 작성 게시글 리스트 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "작성한 게시글 가져오기 성공"),
            @ApiResponse(responseCode = "400", description = "탈퇴한 유저, 리다이렉트 해줄 것"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 사용자입니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/{loginId}/profile")
    public ApplicationResponse<List<PostResponse>> getMemberPagePosts(@PathVariable String loginId) {
        List<PostResponse> responses = postService.getMemberPagePosts(loginId);
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "post")
    @Operation(summary = "홈페이지 전용 Top 게시글 가져오기", description = "Condition(like or latest)에 게시글을 5개 가져온다.<br>" +
            "핫게시글 : 2주 이내에 게시글 중 좋아요가 높은 게시글<br>" +
            "최신게시글 : 1주 이내에 게시글 중 최신 게시글")
    @ResponseStatus(OK)
    @GetMapping("/top/{condition}")
    public ApplicationResponse<List<PostResponse>> getTopFivePostByCondition(@PathVariable @EnumFormat(enumClass = TopPostCondition.class)
                                                                             TopPostCondition condition) {
        List<PostResponse> responses = postService.getTopFivePostByCondition(condition);
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "post")
    @Operation(summary = "홈페이지 전용 공지사항 가져오기")
    @ResponseStatus(OK)
    @GetMapping("/notice")
    public ApplicationResponse<List<PostResponse>> getNotice() {
        List<PostResponse> responses = postService.getNotice();
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "post")
    @Operation(summary = "게시판 검색", description = "게시판 검색, 게시판을 기준으로 조건에 맞춰서 검색한다.")
    @ResponseStatus(OK)
    @GetMapping("/search")
    public ApplicationResponse<SearchResponse> searchPostsWithCondition(@ModelAttribute @Valid SearchRequest request) {
        SearchResponse<PostResponse> responses = postService
                .searchPostsWithCondition(request.getBoard(), request.getKeyword(), request.getPeriod(), request.getOption());
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "post")
    @Operation(summary = "통합 검색", description = "통합 검색, 게시판 상관 없이 제목+내용으로 검색한다.")
    @ResponseStatus(OK)
    @GetMapping("/integration-search")
    public ApplicationResponse<SearchResponse> integrationSearch(@RequestParam @NotBlank String keyword) {
        SearchResponse<PostResponse> response = postService.integrationSearch(keyword);
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 작성하기", description = "[토큰필요]")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 작성 성공"),
            @ApiResponse(responseCode = "401", description = "로그인한 유저만 진행 가능"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판입니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping
    public ApplicationResponse<URI> createPost(@RequestBody @Valid PostRequest request) {
        Long id = postService.create(SecurityUtils.getMemberId(), postMapper.toMetaData(request));
        URI uri = UriCreator.createURI(BASE_URL, id);
        return new ApplicationResponse<>(uri);
    }

    @Tag(name = "post")
    @Operation(summary = "댓글 작성하기", description = "[토큰필요]")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 작성 성공"),
            @ApiResponse(responseCode = "401", description = "로그인한 유저만 진행 가능"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물입니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/{id}/reply")
    public ApplicationResponse createReply(@PathVariable Long id,
                                           @RequestBody @Valid CreateReplyRequest request) {
        postService.commentReply(SecurityUtils.getMemberId(), id, request.getContent());
        return new ApplicationResponse<>();
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 좋아요", description = "[토큰필요]")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 좋아요 성공"),
            @ApiResponse(responseCode = "401", description = "로그인한 유저만 진행 가능"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물입니다."),
            @ApiResponse(responseCode = "409", description = "이미 좋아요를 눌렀습니다.")
    })
    @ResponseStatus(CREATED)
    @PostMapping("/{id}/like")
    public ApplicationResponse<LikeResponse> likePost(@PathVariable Long id) {
        int count = postService.like(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>(LikeResponse.liked(count));
    }

    @Tag(name = "post")
    @Operation(summary = "댓글 좋아요", description = "[토큰필요]")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "댓글 좋아요 성공"),
            @ApiResponse(responseCode = "401", description = "로그인한 유저만 진행 가능"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다."),
            @ApiResponse(responseCode = "409", description = "이미 좋아요를 눌렀습니다."),
    })
    @ResponseStatus(CREATED)
    @PostMapping("/replies/{id}/like")
    public ApplicationResponse<LikeResponse> likeReply(@PathVariable("id") Long replyId) {
        int count = postService.likeReply(SecurityUtils.getMemberId(), replyId);
        return new ApplicationResponse<>(LikeResponse.liked(count));
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 수정하기", description = "[토큰필요] 작성자만 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "작성자가 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물입니다.")
    })
    @ResponseStatus(OK)
    @PutMapping("/{id}")
    public ApplicationResponse updatePost(@PathVariable Long id,
                                          @RequestBody @Valid PostRequest request) {
        postService.update(SecurityUtils.getMemberId(), id, postMapper.toMetaData(request));
        return new ApplicationResponse<>();
    }

    @Tag(name = "post")
    @Operation(summary = "댓글 수정하기", description = "[토큰필요] 작성자만 수정할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 수정 성공"),
            @ApiResponse(responseCode = "400", description = "작성자가 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.")
    })
    @ResponseStatus(OK)
    @PutMapping("/replies/{id}")
    public ApplicationResponse updateReply(@PathVariable("id") Long replyId,
                                           @RequestBody @Valid UpdateReplyRequest request) {
        postService.updateReply(SecurityUtils.getMemberId(), replyId, request.getNewContent());
        return new ApplicationResponse<>();
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 삭제하기", description = "[토큰필요] 작성자만 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "작성자가 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물입니다.")
    })
    @ResponseStatus(OK)
    @PatchMapping("/{id}")
    public ApplicationResponse deletePost(@PathVariable Long id) {
        postService.delete(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>();
    }

    @Tag(name = "post")
    @Operation(summary = "댓글 삭제하기", description = "[토큰필요] 작성자만 삭제할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 삭제 성공"),
            @ApiResponse(responseCode = "400", description = "작성자가 아닙니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/{postId}/replies/{replyId}")
    public ApplicationResponse deleteReply(@PathVariable Long postId, @PathVariable Long replyId) {
        postService.deleteReply(SecurityUtils.getMemberId(), postId, replyId);
        return new ApplicationResponse<>();
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 좋아요 취소하기", description = "[토큰필요] 좋아요한 사람만 취소할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "좋아요를 하지 않았습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시물입니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/{id}/like")
    public ApplicationResponse<LikeResponse> cancelLikePost(@PathVariable Long id) {
        int count = postService.dislike(SecurityUtils.getMemberId(), id);
        return new ApplicationResponse<>(LikeResponse.disliked(count));
    }

    @Tag(name = "post")
    @Operation(summary = "댓글 좋아요 취소하기", description = "[토큰필요] 좋아요한 사람만 취소할 수 있다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "댓글 좋아요 취소 성공"),
            @ApiResponse(responseCode = "400", description = "좋아요를 하지 않았습니다."),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 댓글입니다.")
    })
    @ResponseStatus(OK)
    @DeleteMapping("/replies/{id}/like")
    public ApplicationResponse<LikeResponse> cancelLikeReply(@PathVariable("id") Long replyId) {
        int count = postService.dislikeReply(SecurityUtils.getMemberId(), replyId);
        return new ApplicationResponse<>(LikeResponse.disliked(count));
    }
}
