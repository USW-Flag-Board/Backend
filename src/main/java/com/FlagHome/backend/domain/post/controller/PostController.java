package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.post.controller.dto.CreatePostRequest;
import com.FlagHome.backend.domain.post.controller.dto.AllPostResponse;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.mapper.PostMapper;
import com.FlagHome.backend.domain.post.service.PostService;
import com.FlagHome.backend.global.common.ApplicationResponse;
import com.FlagHome.backend.global.utility.SecurityUtils;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import java.net.URI;

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
    @ResponseStatus(OK)
    @GetMapping
    public ApplicationResponse<AllPostResponse> getPost() {
        return new ApplicationResponse();
    }

    @ResponseStatus(OK)
    @GetMapping("/{id}")
    public ApplicationResponse<AllPostResponse> getPost(@PathVariable("id") @Valid long postId) {
        return new ApplicationResponse();
    }

    @ResponseStatus(CREATED)
    @PostMapping
    public ApplicationResponse<URI> create(@RequestBody @Valid CreatePostRequest createPostRequest) {
        Post post = postMapper.CreateRequestToEntity(createPostRequest);
        postService.create(SecurityUtils.getMemberId(), post, createPostRequest.getBoardName());
        URI uri = UriCreator.createURI(BASE_URL, post.getId());
        return new ApplicationResponse(uri);
    }
}
