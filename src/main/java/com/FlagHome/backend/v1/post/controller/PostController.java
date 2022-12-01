package com.FlagHome.backend.v1.post.controller;

import com.FlagHome.backend.v1.post.dto.PostDto;
import com.FlagHome.backend.v1.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/create")
    public ResponseEntity<PostDto> createPost(@RequestBody PostDto postDto) {
        postService.createPost(postDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/get")
    public ResponseEntity<PostDto> getPost(@RequestParam(value = "postId") long postId) {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPost(postId));
    }

    @PutMapping("/update")
    public ResponseEntity<Void> updatePost(@RequestBody PostDto postDto) {
        postService.updatePost(postDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/delete/{post_id}")
    public ResponseEntity<Void> deletePost(@PathVariable(name = "post_id") long postId) {
        postService.deletePost(postId);
        return ResponseEntity.noContent().build();
    }
}
