package com.FlagHome.backend.domain.reply.controller;

import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@WithMockUser
class ReplyControllerTest {
    private final String baseUrl = "/api/reply";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;

    private Member dummyMember;

    @BeforeEach
    public void testSetup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        dummyMember = memberRepository.save(Member.builder()
                .loginId("gilgil")
                .password("hohho")
                .email("gildong@naver.com")
                .name("honggildong")
                .build());
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    public void createReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().member(dummyMember).title("???????????????").replyList(new ArrayList<>()).content("?????????????????????").build());

        ReplyDto replyDto = new ReplyDto();
        replyDto.setMemberId(dummyMember.getId());
        replyDto.setPostId(dummyPost.getId());
        replyDto.setReplyGroup(1);
        replyDto.setReplyOrder(2);
        replyDto.setReplyDepth(3);
        replyDto.setContent("testReplyContent");
        String jsonBody = objectMapper.writeValueAsString(replyDto);

        mockMvc.perform(post(baseUrl)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("content", is("testReplyContent")))
                .andExpect(jsonPath("replyGroup", is(1)))
                .andExpect(jsonPath("replyOrder", is(2)))
                .andExpect(jsonPath("replyDepth", is(3)))
                .andDo(print());

        postRepository.flush();

        Reply createdReply = dummyPost.getReplyList().get(0);
        assert createdReply.getContent().equals("testReplyContent");
        assert createdReply.getReplyGroup().equals(1L);
        assert createdReply.getReplyOrder().equals(2L);
        assert createdReply.getReplyDepth().equals(3L);
    }

    @Test
    @DisplayName("PostID??? ?????? ?????? ?????????")
    public void findRepliesByPostIdTest() throws Exception {
        Post savedPost = postRepository.save(Post.builder().member(dummyMember).title("????????????").replyList(new ArrayList<>()).content("????????????").build());

        for(int i = 0; i < 4; ++i) {
            Reply reply = Reply.builder().post(savedPost).member(dummyMember).content(i + "??????").replyGroup(1L).replyOrder((long)i).replyDepth(1L).build();
            savedPost.getReplyList().add(reply);
        }
        postRepository.flush();

        String postId = Long.toString(savedPost.getId());
        MvcResult mvcResult = mockMvc.perform(get(baseUrl)
                .param("id", postId))
                .andExpect(status().isOk())
                .andReturn();

        List<ReplyDto> foundReplies = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        for(long i = 0; i < 4; ++i) {
            ReplyDto replyDto = foundReplies.get((int)i);
            assert replyDto.getContent().equals(i + "??????");
            assert replyDto.getReplyOrder() == i;
        }
    }

    @Test
    @DisplayName("?????? ?????? ?????? ?????????")
    public void deleteReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("????????????").member(dummyMember).replyList(new ArrayList<>()).content("????????????").build());
        Reply reply = Reply.builder().member(dummyMember).post(dummyPost).replyGroup(1L).replyDepth(0L).replyOrder(0L).build();
        dummyPost.getReplyList().add(reply);
        postRepository.flush();

        long savedReplyId = dummyPost.getReplyList().get(0).getId();

        mockMvc.perform(delete(baseUrl + "/" + savedReplyId))
                .andExpect(status().isNoContent())
                .andDo(print());

        postRepository.flush();

        Reply isDelete = replyRepository.findById(savedReplyId).orElse(null);
        assert (isDelete == null);
    }

    @Test
    @DisplayName("Depth??? 0??? ?????? ?????? ?????????")
    public void deleteDepthZeroReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("????????????").member(dummyMember).replyList(new ArrayList<>()).content("????????????").build());
        for(int i = 0; i < 3; ++i) {
            Reply reply = Reply.builder().member(dummyMember).post(dummyPost).content(i + "?????? ??????").replyGroup((long)i).replyDepth(0L).replyOrder(0L).build();
            dummyPost.getReplyList().add(reply);
        }
        postRepository.flush();

        Reply targetReply = dummyPost.getReplyList().get(1);
        mockMvc.perform(delete(baseUrl + "/" + targetReply.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());

        postRepository.flush();

        List<Reply> afterReplies = replyRepository.findByPostId(dummyPost.getId());
        for(Reply reply : afterReplies)
            assert !reply.getContent().equals("1?????? ??????");
    }

    @Test
    @DisplayName("???????????? Order??? ??? ????????? ?????? ????????? ?????? ?????????")
    public void deleteNotZeroOrderReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("????????????").member(dummyMember).replyList(new ArrayList<>()).content("????????????").build());
        for(int i = 0; i < 3; ++i) {
            Reply reply = Reply.builder().member(dummyMember).post(dummyPost).content(i + "?????? ??????").replyGroup(0L).replyDepth(1L).replyOrder((long)i).build();
            dummyPost.getReplyList().add(reply);
        }
        postRepository.flush();

        Reply targetReply = dummyPost.getReplyList().get(1);
        mockMvc.perform(delete(baseUrl + "/" + targetReply.getId()))
                .andExpect(status().isNoContent())
                .andDo(print());

        postRepository.flush();

        List<Reply> afterReplies = replyRepository.findByPostId(dummyPost.getId());
        for(Reply reply : afterReplies)
            assert !reply.getContent().equals("1?????? ??????");
    }

    @Test
    @DisplayName("?????? ?????? ?????????")
    public void updateReplyTest() throws Exception {
        final String originalContent = "????????????";
        final String modifiedContent = "????????????";

        Post dummyPost = postRepository.save(Post.builder().title("????????????").member(dummyMember).replyList(new ArrayList<>()).content("????????????").build());
        Reply reply = Reply.builder().post(dummyPost).member(dummyMember).content(originalContent).build();
        dummyPost.getReplyList().add(reply);
        postRepository.flush();

        ReplyDto replyDto = new ReplyDto();
        replyDto.setId(dummyPost.getReplyList().get(0).getId());
        replyDto.setPostId(dummyPost.getId());
        replyDto.setContent(modifiedContent);
        String jsonBody = objectMapper.writeValueAsString(replyDto);

        mockMvc.perform(patch(baseUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andDo(print());

        postRepository.flush();
        replyRepository.flush();

        Reply modifiedReply = replyRepository.findById(dummyPost.getReplyList().get(0).getId()).orElse(null);
        assert modifiedReply != null;
        assert modifiedReply.getContent().equals(modifiedContent);
    }
}