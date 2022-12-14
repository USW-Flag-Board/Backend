package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.Status;
import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.service.PostService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
public class PostControllerTestAsSlice {
    private static final String baseURL = "/api/post";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    private Board dummyBoard;
    private Member dummyMember;
    private Post dummyPost;

    @BeforeEach
    public void testSetting() {
        dummyBoard = new Board();
        dummyBoard.setId(1L);
        dummyMember = Member.builder().id(1L).name("gildong").email("gildong@naver.com").loginId("gildong123").password("123123").phoneNumber("010-444-4444").build();
        dummyPost = new Post(1L, dummyMember, "????????????", "????????????", new ArrayList<>(), dummyBoard, Status.ON, 444L);
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    public void createPostTest() throws Exception {
        // given
        PostDto postDto = new PostDto(dummyPost);
        String jsonBody = objectMapper.writeValueAsString(postDto);

        given(postService.createPost(postDto)).willReturn(postDto);

        // when
        ResultActions actions = mockMvc.perform(post(baseURL)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(redirectedUrl(baseURL + "/" + postDto.getId()));
    }

    @Test
    @DisplayName("????????? ???????????? ?????????")
    public void getPostTest() throws Exception {
        // given
        PostDto returnPostDto = new PostDto(dummyPost);
        given(postService.getPost(dummyPost.getId())).willReturn(returnPostDto);

        // when
        ResultActions actions = mockMvc.perform(get(baseURL)
                .param("postId", Long.toString(dummyPost.getId())));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(dummyPost.getId()))
                .andExpect(jsonPath("userId").value(dummyMember.getId()))
                .andExpect(jsonPath("title").value("????????????"))
                .andExpect(jsonPath("content").value("????????????"))
                .andExpect(jsonPath("boardId").value(dummyBoard.getId()))
                .andExpect(jsonPath("viewCount").value(dummyPost.getViewCount()));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    public void updatePostTest() throws Exception {
        // given
        PostDto postDto = new PostDto(dummyPost);
        given(postService.updatePost(postDto)).willReturn(postDto);

        String jsonBody = objectMapper.writeValueAsString(postDto);

        // when
        ResultActions actions = mockMvc.perform(patch(baseURL)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(dummyPost.getId()))
                .andExpect(jsonPath("userId").value(dummyMember.getId()))
                .andExpect(jsonPath("title").value("????????????"))
                .andExpect(jsonPath("content").value("????????????"))
                .andExpect(jsonPath("boardId").value(dummyBoard.getId()))
                .andExpect(jsonPath("viewCount").value(dummyPost.getViewCount()));
    }

    @Test
    @DisplayName("????????? ?????? ?????????")
    public void deletePostTest() {
        // given
        doNothing().when(postService).deletePost(dummyPost.getId());

        // when
        postService.deletePost(dummyPost.getId());

        // then
        verify(postService, times(1)).deletePost(dummyPost.getId());
    }
}
