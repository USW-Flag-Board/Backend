package com.FlagHome.backend.module.post.controller;

import com.FlagHome.backend.global.common.ApplicationResponse;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import com.FlagHome.backend.module.post.controller.dto.request.CreateReplyRequest;
import com.FlagHome.backend.module.post.controller.dto.request.PostRequest;
import com.FlagHome.backend.module.post.controller.dto.request.SearchRequest;
import com.FlagHome.backend.module.post.controller.dto.request.UpdateReplyRequest;
import com.FlagHome.backend.module.post.controller.dto.response.*;
import com.FlagHome.backend.module.post.controller.mapper.PostMapper;
import com.FlagHome.backend.module.post.domain.enums.TopPostCondition;
import com.FlagHome.backend.module.post.service.PostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Tag(name = "post", description = "게시글 API")
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final static String BASE_URL = "/posts";
    private final PostService postService;
    private final PostMapper postMapper;

    /**
     * Version 1
     */
    /* @Tag(name = "post")
    @Operation(summary = "게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "파라미터로 준 유저 또는 카테고리 에러 입니다.")
    })
    @PostMapping
    public ResponseEntity<ApplicationResponse> createPost(@RequestBody CreatePostRequest postPostDto) {
        long id = postService.createPost(postPostDto);
        URI uri = UriCreator.createURI(BASE_URL, id);
        ApplicationResponse apiResponse = ApplicationResponse.of(uri, HttpStatus.CREATED, "게시글 생성에 성공 하였습니다.");
        return ResponseEntity.ok(apiResponse);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 가져오기에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "요청하신 postId에 일치하는 Post가 존재하지 않습니다.")
    })
    @GetMapping
    public ResponseEntity<ApplicationResponse> getPost(@RequestParam(value = "id") long postId) {
        ApplicationResponse applicationResponse = ApplicationResponse.of(postService.getPost(postId), OK, "게시글 가져오기에 성공 하였습니다.");
        return ResponseEntity.ok(applicationResponse);
    }

    @Tag(name = "post")
    @Operation(summary = "멤버 페이지 작성한 게시글 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "멤버가 작성한 게시글을 가져왔습니다.")
    })
    @ResponseStatus(OK)
    @GetMapping("/{loginId}")
    public ApplicationResponse<List<LightPostDto>> getMemberPostByLoginId(@PathVariable String loginId) {
        List<LightPostDto> response = postService.getMemberPostByLoginId(loginId);
        return new ApplicationResponse<>(response);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "대상이 되는 게시글 또는 카테고리를 찾을수 없습니다.")
    })
    @PatchMapping
    public ResponseEntity<ApplicationResponse> updatePost(@RequestBody CreatePostRequest postDto) {
        ApplicationResponse applicationResponse = ApplicationResponse.of(postService.updatePost(postDto), OK, "게시글 수정에 성공 하였습니다.");
        return ResponseEntity.ok(applicationResponse);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 삭제")
    @ApiResponse(responseCode = "204", description = "게시글 삭제에 성공 하였습니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<ApplicationResponse> deletePost(@PathVariable(name = "id") long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.NO_CONTENT, "게시글 삭제에 성공 하였습니다."));
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 좋아요",
                description = "target-id = 좋아요를 할 게시글의 id\n\n" +
                                "member-id = 서버에서 준 member의 고유ID를 넣으면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 좋아요를 하였습니다."),
            @ApiResponse(responseCode = "400", description = "게시글 좋아요 에러가 발생하였습니다.")
    })
    @PostMapping("/like")
    public ResponseEntity<ApplicationResponse> likePost(@RequestBody LikeDto likeDto) {
        likeService.like(likeDto.getMemberId(), likeDto.getTargetId(), LikeType.POST);
        return ResponseEntity.ok(ApplicationResponse.of(true, OK, "게시글 좋아요를 하였습니다."));
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 좋아요 취소",
                description = "target-id = 좋아요를 할 게시글의 Id\n\n" +
                        "member-id = 서버에서 준 member의 고유ID를 넣으면 됩니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 좋아요 취소를 하였습니다."),
            @ApiResponse(responseCode = "400", description = "게시글 좋아요 취소 에러가 발생하였습니다.")
    })
    @DeleteMapping("/like")
    public ResponseEntity<ApplicationResponse> unlikePost(@RequestParam(value = "member-id") long memberId,
                                                  @RequestParam(value = "target-id") long targetId) {
        likeService.unlike(memberId, targetId, LikeType.POST);
        return ResponseEntity.ok(ApplicationResponse.of(true, HttpStatus.NO_CONTENT, "게시글 좋아요를 취소 하였습니다."));
    }

    @Tag(name = "post")
    @Operation(summary = "최신날짜 + 좋아요갯수 를 기준으로 상위 N개의 게시글을 줍니다.")
    @ApiResponse(responseCode = "200", description = "상위 N개의 게시글을 가져왔습니다.")
    @GetMapping("/top")
    public ResponseEntity<ApplicationResponse> getTopNPostListByDateAndLike(@RequestParam(value = "post-count") int postCount) {
        String message = "상위 " + postCount + "개의 게시글을 가져왔습니다.";
        return ResponseEntity.ok(ApplicationResponse.of(postService.getTopNPostListByDateAndLike(postCount), OK, message));
    } */
    /**
     * Version 2
     */
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
    @Operation(summary = "게시글 모두 가져오기", description = "게시판 이름에 맞춰서 페이징 처리")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시판의 모든 게시글 불러오기 성공"),
            @ApiResponse(responseCode = "404", description = "존재하지 않는 게시판입니다.")
    })
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<Page<PostResponse>> getAllPostsByBoard(@RequestParam("board") String boardName, Pageable pageable) {
        Page<PostResponse> response = postService.getAllPostsByBoard(boardName, pageable);
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
    @Operation(summary = "홈페이지 전용 Top 게시글 가져오기", description = "Condition(like or latest)에 따라 핫 게시글 또는 최신 게시글 5개를 가져온다.")
    @ResponseStatus(OK)
    @GetMapping("/top/{condition}")
    public ApplicationResponse<List<PostResponse>> getTopFivePostByCondition(@PathVariable TopPostCondition condition) {
        List<PostResponse> responses = postService.getTopFivePostByCondition(condition);
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "post")
    @Operation(summary = "게시판 검색", description = "게시판 검색, 게시판을 기준으로 조건에 맞춰서 검색한다.")
    @ResponseStatus(OK)
    @GetMapping("/search")
    public ApplicationResponse<SearchResponse> searchPostsWithCondition(@ModelAttribute @Valid SearchRequest request) {
        SearchResponse responses = postService
                .searchPostsWithCondition(request.getBoard(), request.getKeyword(), request.getPeriod(), request.getOption());
        return new ApplicationResponse<>(responses);
    }

    @Tag(name = "post")
    @Operation(summary = "통합 검색", description = "통합 검색, 게시판 상관 없이 제목+내용으로 검색한다.")
    @ResponseStatus(OK)
    @GetMapping("/integration-search")
    public ApplicationResponse<SearchResponse> integrationSearch(@RequestParam("keyword") String keyword) {
        SearchResponse response = postService.integrationSearch(keyword);
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
        Long id = postService.create(SecurityUtils.getMemberId(), postMapper.mapFrom(request), request.getBoardName()).getId();
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
        postService.update(SecurityUtils.getMemberId(), id, postMapper.mapFrom(request));
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
