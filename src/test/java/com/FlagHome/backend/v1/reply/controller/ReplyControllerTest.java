package com.FlagHome.backend.v1.reply.controller;

import com.FlagHome.backend.v1.member.entity.Member;
import com.FlagHome.backend.v1.member.repository.MemberRepository;
import com.FlagHome.backend.v1.post.entity.Post;
import com.FlagHome.backend.v1.post.repository.PostRepository;
import com.FlagHome.backend.v1.reply.dto.ReplyDto;
import com.FlagHome.backend.v1.reply.entity.Reply;
import com.FlagHome.backend.v1.reply.repository.ReplyRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
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
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class ReplyControllerTest {
    private final String baseUrl = "/reply";

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
    @Mock
    private Post mockPost;

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
    @DisplayName("댓글 생성 테스트")
    public void createReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().member(dummyMember).title("가짜포스트").content("가짜포스트내용").build());

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
    }

    @Test
    @DisplayName("PostID로 댓글 조회 테스트")
    public void findRepliesByPostIdTest() throws Exception {
        Post post = Post.builder().member(dummyMember).title("제목이다").content("내용이다").build();
        Post savedPost = postRepository.save(post);

        for(int i = 0; i < 4; ++i) {
            Reply reply = Reply.builder().post(savedPost).member(dummyMember).content(i + "번째").replyGroup(1L).replyOrder((long)i).replyDepth(1L).build();
            replyRepository.save(reply);
        }

        String postId = Long.toString(post.getId());
        MvcResult mvcResult = mockMvc.perform(get(baseUrl)
                .param("id", postId))
                .andExpect(status().isOk())
                .andReturn();

        List<ReplyDto> foundReplies = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {});
        for(long i = 0; i < 4; ++i) {
            ReplyDto replyDto = foundReplies.get((int)i);
            assert replyDto.getContent().equals(i + "번째");
            assert replyDto.getReplyOrder() == i;
        }
    }

    @Test
    @DisplayName("단일 댓글 삭제 테스트")
    public void deleteReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).content("더미내용").build());
        Reply reply = Reply.builder().member(dummyMember).post(dummyPost).replyGroup(1L).replyDepth(0L).replyOrder(0L).build();
        Reply savedReply = replyRepository.save(reply);
        long savedReplyId = savedReply.getId();

        mockMvc.perform(delete(baseUrl + "/" + savedReplyId)
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        Reply isDelete = replyRepository.findById(savedReplyId).orElse(null);
        assert (isDelete == null);
    }

    @Test
    @DisplayName("Depth가 0인 댓글 삭제 테스트")
    public void deleteDepthZeroReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).content("더미내용").build());
        ArrayList<Reply> savedReplyList = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
            Reply reply = Reply.builder().member(dummyMember).post(dummyPost).content(i + "번째 내용").replyGroup((long)i).replyDepth(0L).replyOrder(0L).build();
            savedReplyList.add(replyRepository.save(reply));
        }

        Reply targetReply = savedReplyList.get(1);
        mockMvc.perform(delete(baseUrl + "/" + targetReply.getId())
                .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        List<Reply> afterReplies = replyRepository.findByPostId(dummyPost.getId());
        Reply checkReply = afterReplies.get(1);
        assert checkReply.getReplyGroup() == 1;
        assert checkReply.getContent().equals("2번째 내용");
    }

    @Test
    @DisplayName("자신보다 Order가 큰 댓글이 있는 댓글의 삭제 테스트")
    public void deleteNotZeroOrderReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).content("더미내용").build());
        ArrayList<Reply> savedReplyList = new ArrayList<>();
        for(int i = 0; i < 3; ++i) {
            Reply reply = Reply.builder().member(dummyMember).post(dummyPost).content(i + "번째 내용").replyGroup(0L).replyDepth(1L).replyOrder((long)i).build();
            savedReplyList.add(replyRepository.save(reply));
        }

        Reply targetReply = savedReplyList.get(1);
        mockMvc.perform(delete(baseUrl + "/" + targetReply.getId())
                        .with(csrf()))
                .andExpect(status().isNoContent())
                .andDo(print());

        List<Reply> afterReplies = replyRepository.findByPostId(dummyPost.getId());
        Reply checkReply = afterReplies.get(1);
        assert checkReply.getReplyOrder() == 1;
        assert checkReply.getContent().equals("2번째 내용");
    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void updateReplyTest() throws Exception {
        final String originalContent = "원래내용";
        final String modifiedContent = "바뀐내용";

        Reply reply = replyRepository.save(Reply.builder().post(mockPost).member(dummyMember).content(originalContent).build());
        assert reply.getContent().equals(originalContent);

        ReplyDto replyDto = new ReplyDto();
        replyDto.setId(reply.getId());
        replyDto.setContent(modifiedContent);
        String jsonBody = objectMapper.writeValueAsString(replyDto);

        mockMvc.perform(put(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andDo(print());

        Reply modifiedReply = replyRepository.findById(replyDto.getId()).orElse(null);
        assert modifiedReply != null;
        assert modifiedReply.getContent().equals(modifiedContent);
    }
}