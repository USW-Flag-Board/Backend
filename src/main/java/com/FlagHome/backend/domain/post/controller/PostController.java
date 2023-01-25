package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.HttpResponse;
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
@RequestMapping("/api/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final static String BASE_URL = "/api/posts";

    @Tag(name = "post")
    @Operation(summary = "게시글 생성")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "게시글 생성에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "파라미터로 준 유저 또는 카테고리 에러 입니다.")
    })
    @PostMapping
    public ResponseEntity<HttpResponse> createPost(@RequestBody PostDto postDto) {
        PostDto createdPostDto = postService.createPost(postDto);
        URI uri = UriCreator.createUri(BASE_URL, createdPostDto.getId());
        HttpResponse httpResponse = HttpResponse.ok(uri, HttpStatus.CREATED, "게시글 생성에 성공 하였습니다.");
        return ResponseEntity.ok(httpResponse);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 가져오기",
            description = "현재 board를 통해 게시글 리스트를 가져오면 게시글의 content와 replyList를 제외한 요소들이 가져와 집니다.\n" +
                    "그러므로 게시글을 진입할때 content와 replyList가 필요한데 그럴때는 get query parameter에 viaBoard = true를 주면\n" +
                    "content와 replyList를 리턴해줍니다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 가져오기에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "요청하신 postId에 일치하는 Post가 존재하지 않습니다.")
    })
    @GetMapping
    public ResponseEntity<HttpResponse> getPost(@RequestParam(value = "postId") long postId,
                                           @RequestParam(value = "viaBoard", required = false) Boolean viaBoard) {
        HttpResponse httpResponse = HttpResponse.ok(postService.getPost(postId, viaBoard), HttpStatus.OK, "게시글 가져오기에 성공 하였습니다.");
        return ResponseEntity.ok(httpResponse);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 수정")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "게시글 수정에 성공 하였습니다."),
            @ApiResponse(responseCode = "404", description = "대상이 되는 게시글 또는 카테고리를 찾을수 없습니다.")
    })
    @PatchMapping
    public ResponseEntity<HttpResponse> updatePost(@RequestBody PostDto postDto) {
        HttpResponse httpResponse = HttpResponse.ok(postService.updatePost(postDto), HttpStatus.OK, "게시글 수정에 성공 하였습니다.");
        return ResponseEntity.ok(httpResponse);
    }

    @Tag(name = "post")
    @Operation(summary = "게시글 삭제")
    @ApiResponse(responseCode = "204", description = "게시글 삭제에 성공 하였습니다.")
    @DeleteMapping("/{post_id}")
    public ResponseEntity<HttpResponse> deletePost(@PathVariable(name = "post_id") long postId) {
        postService.deletePost(postId);
        return ResponseEntity.ok(HttpResponse.ok(true, HttpStatus.NO_CONTENT, "게시글 삭제에 성공 하였습니다."));
    }
}
