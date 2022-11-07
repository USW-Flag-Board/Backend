package com.FlagHome.backend.v1.reply.controller;

import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.reply.dto.ReplyDto;
import com.FlagHome.backend.v1.user.entity.User;
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

import javax.transaction.Transactional;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class ReplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Mock
    private User mockUser;

    @Mock
    private Post mockPost;

    private final String baseUrl = "/reply";

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createReplyTest() throws Exception {
        ReplyDto replyDto = new ReplyDto();
        replyDto.setUserId(mockUser.getId());
        replyDto.setPostId(mockPost.getId());
        replyDto.setReplyGroup(1);
        replyDto.setReplyOrder(2);
        replyDto.setReplyDepth(3);
        replyDto.setContent("testReplyContent");

        ObjectMapper objectMapper = new ObjectMapper();
        String jsonBody = objectMapper.writeValueAsString(replyDto);

        mockMvc.perform(post(baseUrl + "/create")
                .with(csrf())
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("content", is("testReplyContent")))
                .andExpect(jsonPath("replyGroup", is(1)))
                .andExpect(jsonPath("replyOrder", is(2)))
                .andExpect(jsonPath("replyDepth", is(3)))
                .andDo(print());
    }
}