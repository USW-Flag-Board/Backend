package com.FlagHome.backend.v1.post.service;

import com.FlagHome.backend.v1.Status;
import com.FlagHome.backend.v1.post.dto.PostDto;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.post.repository.PostRepository;
import com.FlagHome.backend.v1.user.entity.User;
import com.FlagHome.backend.v1.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class PostService {
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private final UserRepository userRepository;

    public void updatePost(PostDto postDto) {
        Post postEntity = postRepository.findById(postDto.getId()).orElse(null);
        assert postEntity != null;

        postEntity.setTitle(postDto.getTitle());
        postEntity.setContent(postDto.getContent());
        postEntity.setCategory(postDto.getCategory());
    }

    public long createPost(PostDto postDto) {
        Optional<User> user = userRepository.findByUserId(postDto.getUserId());

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을 수 없습니다.");
        }

        Post post = Post.builder()
                .user(user.get())
                .title(postDto.getTitle())
                .content(postDto.getContent())
                .status(Status.NORMAL)
                .category(postDto.getCategory())
                .build();

        postRepository.save(post);

        return post.getId();
    }
}
