package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        postService.createPost(postDto);
        return ResponseEntity.ok().build();
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
