package com.FlagHome.backend.v1.post.controller;

import com.FlagHome.backend.v1.post.dto.PostDto;
import com.FlagHome.backend.v1.post.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/post")
    public ResponseEntity<Void> getPost(@RequestBody PostDto postDto) {
        postService.createPost(postDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/post/{id}")
    public ResponseEntity<Long> putPost(@RequestBody PostDto postDto) {
        postService.createPost(postDto);//여기 분명 코드를 넣어야 하는데 뭘 넣어야 할지 모르겠어요ㅠㅠ...
        return ResponseEntity.ok().build();
    }
}
