package com.FlagHome.backend.v1.post.service;

import com.FlagHome.backend.v1.post.dto.PostDto;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.post.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public void updatePost(PostDto postDto) {
        Post postEntity = postRepository.findById(postDto.getId()).orElse(null);
        assert postEntity != null;

        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setCategory(postDto.getCategory());
    }
}
