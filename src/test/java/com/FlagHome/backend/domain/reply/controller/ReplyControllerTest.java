package com.FlagHome.backend.domain.reply.controller;

import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.like.entity.LikeDto;
import com.FlagHome.backend.domain.like.enums.LikeType;
import com.FlagHome.backend.domain.like.repository.LikeRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.reply.dto.ReplyDto;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
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
    private final static String BASE_URL = "/api/replies";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private LikeRepository likeRepository;
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

        setJwtInformation(dummyMember.getId());
    }

    @Test
    @DisplayName("댓글 생성 테스트")
    public void createReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().member(dummyMember).title("가짜포스트").replyList(new ArrayList<>()).content("가짜포스트내용").build());

        ReplyDto replyDto = new ReplyDto();
        replyDto.setMemberId(dummyMember.getId());
        replyDto.setPostId(dummyPost.getId());
        replyDto.setReplyGroup(1);
        replyDto.setReplyOrder(2);
        replyDto.setReplyDepth(3);
        replyDto.setLikeCount(0L);
        replyDto.setContent("testReplyContent");
        String jsonBody = objectMapper.writeValueAsString(replyDto);

        mockMvc.perform(post(BASE_URL)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("status", is("CREATED")))
                    .andExpect(jsonPath("message", is("댓글 생성을 완료 하였습니다.")))
                    .andDo(print());

        postRepository.flush();

        Reply createdReply = dummyPost.getReplyList().get(0);
        assert createdReply.getContent().equals("testReplyContent");
        assert createdReply.getReplyGroup().equals(1L);
        assert createdReply.getReplyOrder().equals(2L);
        assert createdReply.getReplyDepth().equals(3L);
    }

    @Test
    @DisplayName("PostID로 댓글 조회 테스트")
    public void findRepliesByPostIdTest() throws Exception {
        Post savedPost = postRepository.save(Post.builder().member(dummyMember).title("제목이다").replyList(new ArrayList<>()).content("내용이다").build());

        for(int i = 0; i < 4; ++i) {
            Reply reply = Reply.builder().post(savedPost).member(dummyMember).content(i + "번째").replyGroup(1L).replyOrder((long)i).replyDepth(1L).likeCount(0L).build();
            savedPost.getReplyList().add(reply);
        }
        postRepository.flush();

        String postId = Long.toString(savedPost.getId());
        mockMvc.perform(get(BASE_URL)
                .param("post-id", postId))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("status", is("OK")))
                    .andExpect(jsonPath("message", is("댓글 리스트 가져오기에 성공 하였습니다.")));
    }

    @Test
    @DisplayName("단일 댓글 삭제 테스트")
    public void deleteReplyTest() throws Exception {
        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).replyList(new ArrayList<>()).content("더미내용").build());
        Reply reply = Reply.builder().member(dummyMember).post(dummyPost).replyGroup(1L).replyDepth(0L).replyOrder(0L).likeCount(0L).build();
        dummyPost.getReplyList().add(reply);
        postRepository.flush();

        long savedReplyId = dummyPost.getReplyList().get(0).getId();

        mockMvc.perform(delete(BASE_URL + "/" + savedReplyId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", is("NO_CONTENT")))
                .andExpect(jsonPath("message", is("댓글 삭제에 성공하였습니다.")));

        postRepository.flush();

        Reply isDelete = replyRepository.findById(savedReplyId).orElse(null);
        assert (isDelete == null);
    }

//    @Test
//    @DisplayName("Depth가 0인 댓글 삭제 테스트")
//    public void deleteDepthZeroReplyTest() throws Exception {
//        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).replyList(new ArrayList<>()).content("더미내용").build());
//        for(int i = 0; i < 3; ++i) {
//            Reply reply = Reply.builder().member(dummyMember).post(dummyPost).content(i + "번째 내용").replyGroup((long)i).replyDepth(0L).replyOrder(0L).build();
//            dummyPost.getReplyList().add(reply);
//        }
//        postRepository.flush();
//
//        Reply targetReply = dummyPost.getReplyList().get(1);
//        mockMvc.perform(delete(baseUrl + "/" + targetReply.getId()))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        postRepository.flush();
//
//        List<Reply> afterReplies = replyRepository.findByPostId(dummyPost.getId());
//        for(Reply reply : afterReplies)
//            assert !reply.getContent().equals("1번째 내용");
//    }

//    @Test
//    @DisplayName("자신보다 Order가 큰 댓글이 있는 댓글의 삭제 테스트")
//    public void deleteNotZeroOrderReplyTest() throws Exception {
//        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).replyList(new ArrayList<>()).content("더미내용").build());
//        for(int i = 0; i < 3; ++i) {
//            Reply reply = Reply.builder().member(dummyMember).post(dummyPost).content(i + "번째 내용").replyGroup(0L).replyDepth(1L).replyOrder((long)i).build();
//            dummyPost.getReplyList().add(reply);
//        }
//        postRepository.flush();
//
//        Reply targetReply = dummyPost.getReplyList().get(1);
//        mockMvc.perform(delete(baseUrl + "/" + targetReply.getId()))
//                .andExpect(status().isNoContent())
//                .andDo(print());
//
//        postRepository.flush();
//
//        List<Reply> afterReplies = replyRepository.findByPostId(dummyPost.getId());
//        for(Reply reply : afterReplies)
//            assert !reply.getContent().equals("1번째 내용");
//    }

    @Test
    @DisplayName("댓글 수정 테스트")
    public void updateReplyTest() throws Exception {
        final String originalContent = "원래내용";
        final String modifiedContent = "바뀐내용";

        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).replyList(new ArrayList<>()).content("더미내용").build());
        Reply reply = Reply.builder().post(dummyPost).member(dummyMember).content(originalContent).build();
        dummyPost.getReplyList().add(reply);
        postRepository.flush();

        ReplyDto replyDto = new ReplyDto();
        replyDto.setId(dummyPost.getReplyList().get(0).getId());
        replyDto.setPostId(dummyPost.getId());
        replyDto.setContent(modifiedContent);
        String jsonBody = objectMapper.writeValueAsString(replyDto);

        mockMvc.perform(patch(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("status", is("OK")))
                    .andExpect(jsonPath("message", is("댓글 수정에 성공 하였습니다.")));

        postRepository.flush();
        replyRepository.flush();

        Reply modifiedReply = replyRepository.findById(dummyPost.getReplyList().get(0).getId()).orElse(null);
        assert modifiedReply != null;
        assert modifiedReply.getContent().equals(modifiedContent);
    }

    @Test
    @DisplayName("댓글 좋아요 테스트")
    public void likeReplyTest() throws Exception {
        // given
        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).replyList(new ArrayList<>()).content("더미내용").build());
        Reply reply = replyRepository.save(Reply.builder().member(dummyMember).post(dummyPost).likeCount(0L).build());

        long userId = dummyMember.getId();
        long replyId = reply.getId();
        String type = "REPLY";

        LikeDto likeDto = new LikeDto(userId, replyId, type);
        String jsonBody = objectMapper.writeValueAsString(likeDto);

        // when
        mockMvc.perform(post(BASE_URL + "/like")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                    .andExpect(status().isOk())
                    .andDo(print());

        // then
        replyRepository.findById(replyId).ifPresent(resultReply -> assertThat(resultReply.getLikeCount()).isEqualTo(1L));
        assertThat(likeRepository.findLikeByUserId(userId).size()).isEqualTo(1);
    }

    @Test
    @DisplayName("댓글 좋아요 취소 테스트")
    public void unlikeReplyTest() throws Exception {
        // given
        Post dummyPost = postRepository.save(Post.builder().title("더미제목").member(dummyMember).replyList(new ArrayList<>()).content("더미내용").build());
        Reply reply = replyRepository.save(Reply.builder().member(dummyMember).post(dummyPost).likeCount(1L).build());

        long userId = dummyMember.getId();
        long replyId = reply.getId();
        likeRepository.save(Like.builder().userId(userId).targetId(replyId).targetType(LikeType.REPLY).build());

        // when
        mockMvc.perform(delete(BASE_URL + "/like")
                .param("user-id", Long.toString(userId))
                .param("target-id", Long.toString(replyId))
                .param("target-type", LikeType.REPLY.toString()))
                    .andDo(print());

        // then
        replyRepository.findById(replyId).ifPresent(resultReply -> assertThat(resultReply.getLikeCount()).isEqualTo(0L));
        assertThat(likeRepository.findLikeByUserId(userId).size()).isEqualTo(0);
    }

    private void setJwtInformation(long memberId) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_USER");

        UserDetails principal = new User(Long.toString(memberId), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}