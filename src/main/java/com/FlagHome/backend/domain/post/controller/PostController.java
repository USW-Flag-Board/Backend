package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.service.PostService;
import com.FlagHome.backend.global.utility.UriCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;
    private final static String BASE_URL = "/api/post";

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostDto postDto) {
        PostDto createdPostDto = postService.createPost(postDto);
        URI uri = UriCreator.createUri(BASE_URL, createdPostDto.getId());
        return ResponseEntity.created(uri).build();
    }

    @GetMapping
    public ResponseEntity<PostDto> getPost(@RequestParam(value = "postId") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @PutMapping
    public ResponseEntity<Void> updatePost(@RequestBody PostDto postDto) {
        postService.updatePost(postDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "post_id") long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
