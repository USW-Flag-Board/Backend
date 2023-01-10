package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.service.PostService;
import com.FlagHome.backend.global.utility.UriCreator;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@Tag(name = "post", description = "게시글 API")
@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final static String BASE_URL = "/api/post";

    @Tag(name = "post")
    @Operation(summary = "게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "파라미터로 준 유저 또는 카테고리 에러 입니다.")
    })
    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostDto postDto) {
        PostDto createdPostDto = postService.createPost(postDto);
        URI uri = UriCreator.createUri(BASE_URL, createdPostDto.getId());
        return ResponseEntity.created(uri).build();
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 가져오기")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 가져오기에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "요청하신 postId에 일치하는 Post가 존재하지 않습니다.")
    })
    @GetMapping
    public ResponseEntity<PostDto> getPost(@RequestParam(value = "postId") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "대상이 되는 게시글 또는 카테고리를 찾을수 없습니다.")
    })
    @PatchMapping
    public ResponseEntity<Void> updatePost(@RequestBody PostDto postDto) {
        postService.updatePost(postDto);
        return ResponseEntity.ok().build();
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 삭제")
    @ApiResponse(responseCode = "204", description = "게시글 삭제에 성공 하였습니다.")
    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "post_id") long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
