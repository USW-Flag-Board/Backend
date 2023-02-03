package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.like.service.LikeService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.service.PostService;
import com.FlagHome.backend.domain.reply.entity.Reply;
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

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PostController.class)
@MockBean(JpaMetamodelMappingContext.class)
@WithMockUser
public class PostControllerTestAsSlice {
    private static final String BASE_URL = "/api/posts";

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PostService postService;

    @MockBean
    private LikeService likeService;

    @Autowired
    private ObjectMapper objectMapper;

    private Board dummyBoard;
    private Member dummyMember;
    private Post dummyPost;
    private Reply dummyReply;

    @BeforeEach
    public void testSetting() {
        dummyBoard = new Board();
        dummyBoard.setId(1L);

        dummyMember = Member.builder().id(1L).name("gildong").email("gildong@naver.com").loginId("gildong123").password("123123").phoneNumber("010-444-4444").build();

        dummyPost = new Post(1L, dummyMember, "제목이다", "내용이다", new ArrayList<>(), dummyBoard, Status.NORMAL, 444L, 0L);

        dummyReply = new Reply(1L, dummyMember, dummyPost, "댓글내용", 1L, 1L, 1L, 0L, Status.NORMAL);
        dummyPost.getReplyList().add(dummyReply);
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    public void createPostTest() throws Exception {
        // given
        PostDto postDto = new PostDto(dummyPost);
        String jsonBody = objectMapper.writeValueAsString(postDto);

        given(postService.createPost(any())).willReturn(postDto);

        // when
        ResultActions actions = mockMvc.perform(post(BASE_URL)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("payload", is(BASE_URL + "/" + postDto.getId())))
                .andExpect(jsonPath("status", is("CREATED")))
                .andExpect(jsonPath("message", is("게시글 생성에 성공 하였습니다.")));
    }

    @Test
    @DisplayName("게시글 가져오기 테스트")
    public void getPostTest() throws Exception {
        // given
        PostDto returnPostDto = new PostDto(dummyPost);
        given(postService.getPost(dummyPost.getId())).willReturn(returnPostDto);

        // when
        ResultActions actions = mockMvc.perform(get(BASE_URL)
                .param("id", Long.toString(dummyPost.getId())));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", is("OK")))
                .andExpect(jsonPath("message", is("게시글 가져오기에 성공 하였습니다.")));
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void updatePostTest() throws Exception {
        // given
        PostDto postDto = new PostDto(dummyPost);
        given(postService.updatePost(any())).willReturn(postDto);

        String jsonBody = objectMapper.writeValueAsString(postDto);

        // when
        ResultActions actions = mockMvc.perform(patch(BASE_URL)
                .with(csrf())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody));

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", is("OK")))
                .andExpect(jsonPath("message", is("게시글 수정에 성공 하였습니다.")));
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePostTest() {
        // given
        doNothing().when(postService).deletePost(dummyPost.getId());

        // when
        postService.deletePost(dummyPost.getId());

        // then
        verify(postService, times(1)).deletePost(dummyPost.getId());
    }
}
