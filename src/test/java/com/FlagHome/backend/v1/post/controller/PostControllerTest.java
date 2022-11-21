package com.FlagHome.backend.v1.post.controller;

import com.FlagHome.backend.v1.post.Category;
import com.FlagHome.backend.v1.post.dto.PostDto;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.post.repository.PostRepository;
import com.FlagHome.backend.v1.user.entity.User;
import com.FlagHome.backend.v1.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class PostControllerTest {

    private final String baseUrl = "/post";

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시글 수정 테스트")
    public void updatePostTest() throws Exception {
        User dummyUser = userRepository.save(User.builder()
                                        .loginId("gildong11")
                                        .password("123123")
                                        .email("gildong@naver.com")
                                        .phoneNumber("010-4444-4444")
                                        .studentId("2").build());

        String originalTitle = "원래제목";
        String originalContent = "원래내용";
        Category originalCategory = Category.study;

        Post postEntity = postRepository.save(Post.builder()
                                        .title(originalTitle)
                                        .content(originalContent)
                                        .category(originalCategory)
                                        .viewCount(0L)
                                        .user(dummyUser)
                                        .build());

        String changedTitle = "바뀐제목";
        String changedContent = "바뀐내용";
        Category changedCategory = Category.free;

        PostDto changedPostDto = new PostDto(postEntity);
        changedPostDto.setTitle(changedTitle);
        changedPostDto.setContent(changedContent);
        changedPostDto.setCategory(changedCategory);

        String jsonBody = objectMapper.writeValueAsString(changedPostDto);

        mockMvc.perform(put(baseUrl + "/update")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andDo(print());

        Post changedPostEntity = postRepository.findById(changedPostDto.getId()).orElse(null);
        assert changedPostEntity != null;
        assert changedPostEntity.getTitle().equals(changedTitle);
        assert changedPostEntity.getContent().equals(changedContent);
        assert changedPostEntity.getCategory().equals(changedCategory);
    }

    @Test
    @DisplayName("게시물작성")
    public void GetPostTest() throws Exception{
        PostDto postDto = new PostDto();
        postDto.setTitle("testPostTitle");
        postDto.setContent("testPostContent");
        Category requestedCategory = Category.notice;
        String jsonBody = objectMapper.writeValueAsString (postDto);

        mockMvc.perform (get(baseUrl + "/get")
               .with(csrf())
               .contentType(MediaType.APPLICATION_JSON))
               .andExpect (jsonPath("title", is("testPostTitle")))
               .andExpect (jsonPath("content", is("testPostContent")))
               .andDo (print());

    }
}