package com.FlagHome.backend.v1.post.controller;

import com.FlagHome.backend.v1.post.dto.PostDto;
import com.FlagHome.backend.v1.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PutMapping("/update")
    public ResponseEntity<Void> updatePost(@RequestBody PostDto postDto) {
        postService.updatePost(postDto);
        return ResponseEntity.ok().build();
    }
}
