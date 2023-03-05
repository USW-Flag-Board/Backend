package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.common.Status;
import com.FlagHome.backend.domain.like.entity.Like;
import com.FlagHome.backend.domain.like.entity.LikeDto;
import com.FlagHome.backend.domain.like.enums.LikeType;
import com.FlagHome.backend.domain.like.repository.LikeRepository;
import com.FlagHome.backend.domain.like.service.LikeService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.CreatePostRequest;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class PostControllerTest {

    private final static String BASE_URL = "/posts";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private ReplyRepository replyRepository;
    @Autowired
    private LikeRepository likeRepository;
    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private LikeService likeService;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private Member dummyMember;
    private Board dummyBoard1, dummyBoard2;


    @BeforeEach
    public void testSetting() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();

        dummyMember = memberRepository.save(Member.builder()
                .loginId("gildong11")
                .password("123123")
                .email("gildong@naver.com")
                .studentId("2").build());
        dummyBoard1 = boardRepository.save(Board.builder()
                        .koreanName("일반게시판")
                        .englishName("board")
                        .boardDepth(0L)
                .build());
        dummyBoard2 = boardRepository.save(Board.builder()
                .koreanName("활동")
                .englishName("activity")
                .boardDepth(1L)
                .parent(dummyBoard1)
                .build());

        setJwtInformation(dummyMember.getId());
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    public void createPostTest() throws Exception {
        CreatePostRequest postDto = new CreatePostRequest();
        postDto.setTitle("테스트 제목");
        postDto.setContent("테스트 내용");
        postDto.setUserId(dummyMember.getId());
        postDto.setBoardId(dummyBoard1.getId());

        mockMvc.perform(post(BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", is("CREATED")))
                .andExpect(jsonPath("message", is("게시글 생성에 성공 하였습니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 가져오기 테스트")
    public void getPostTest() throws Exception {
        String title = "테스트 제목";
        String content = "테스트 내용";

        Post postEntity = postRepository.save(Post.builder()
                .title(title)
                .content(content)
                .viewCount(0L)
                .replyList(new ArrayList<>())
                .likeList(new ArrayList<>())
                .member(dummyMember)
                .board(dummyBoard2)
                .build());

        mockMvc.perform(get(BASE_URL)
                        .param("id", Long.toString(postEntity.getId())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("status", is("OK")))
                .andExpect(jsonPath("message", is("게시글 가져오기에 성공 하였습니다.")))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void updatePostTest() throws Exception {
        String originalTitle = "원래제목";
        String originalContent = "원래내용";
        Board originalBoard = dummyBoard1;

        Post postEntity = postRepository.save(Post.builder()
                                        .title(originalTitle)
                                        .content(originalContent)
                                        .board(originalBoard)
                                        .viewCount(0L)
                                        .replyList(new ArrayList<>())
                                        .likeList(new ArrayList<>())
                                        .member(dummyMember)
                                        .build());

        String changedTitle = "바뀐제목";
        String changedContent = "바뀐내용";

        Board changedBoard = dummyBoard2;

        PostDto changedPostDto = new PostDto(postEntity);
        changedPostDto.setTitle(changedTitle);
        changedPostDto.setContent(changedContent);
        changedPostDto.setBoardId(changedBoard.getId());

        String jsonBody = objectMapper.writeValueAsString(changedPostDto);

        mockMvc.perform(patch(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("status", is("OK")))
                    .andExpect(jsonPath("message", is("게시글 수정에 성공 하였습니다.")))
                    .andDo(print());

        Post changedPostEntity = postRepository.findById(changedPostDto.getId()).orElse(null);
        assert changedPostEntity != null;
        assert changedPostEntity.getTitle().equals(changedTitle);
        assert changedPostEntity.getContent().equals(changedContent);
        assert changedPostEntity.getBoard().equals(changedBoard);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePostTest() throws Exception {
        Post postEntity = postRepository.save(Post.builder()
                        .title("삭제될 게시글 제목")
                        .content("삭제될 게시글 내용")
                        .board(dummyBoard2)
                        .member(dummyMember)
                        .build());

        long deleteTargetPostId = postEntity.getId();

        mockMvc.perform(delete(BASE_URL + "/" + deleteTargetPostId))
                .andExpect(jsonPath("status", is("NO_CONTENT")))
                .andExpect(jsonPath("message", is("게시글 삭제에 성공 하였습니다.")))
                .andDo(print());

        Post post = postRepository.findById(deleteTargetPostId).orElse(null);
        assert post == null;
    }

    @Test
    @DisplayName("게시글 좋아요 테스트")
    public void likePostTest() throws Exception {
        // given
        Post post = postRepository.save(Post.builder()
                .member(dummyMember)
                .board(dummyBoard1)
                .title("이게제목")
                .content("이게 내용")
                .status(Status.NORMAL)
                .viewCount(0L)
                .replyList(new ArrayList<>())
                .likeList(new ArrayList<>())
                .build());

        long postId = post.getId();
        long memberId = dummyMember.getId();
        LikeDto likeDto = new LikeDto(memberId, postId);
        String jsonBody = objectMapper.writeValueAsString(likeDto);

        // when
        mockMvc.perform(post(BASE_URL + "/like")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                    .andExpect(status().isOk())
                    .andDo(print());

        // then
        List<Like> likeList = likeRepository.findAll();
        assertThat(likeList.size()).isEqualTo(1);
    }

    @Test
    @DisplayName("게시글 좋아요 취소 테스트")
    public void unlikePostTest() throws Exception {
        // given
        Post post = postRepository.save(Post.builder()
                .member(dummyMember)
                .board(dummyBoard1)
                .title("이게제목")
                .content("이게 내용")
                .status(Status.NORMAL)
                .viewCount(0L)
                .replyList(new ArrayList<>())
                .likeList(new ArrayList<>())
                .build());

        long postId = post.getId();
        long memberId = dummyMember.getId();
        likeService.like(memberId, postId, LikeType.POST);

        // when
        mockMvc.perform(delete(BASE_URL + "/like")
                .param("member-id", Long.toString(memberId))
                .param("target-id", Long.toString(postId)))
                    .andExpect(status().isOk())
                    .andDo(print());

        // then
        assertThat(likeRepository.findAll().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("최신날짜+좋아요 상위 Top3 게시글 가져오기 테스트")
    public void getTop3PostListByDateAndLikeTest() throws Exception {
        // given
        Post firstPost = postRepository.save(Post.builder()
                .member(dummyMember)
                .title("첫째 게시물 제목")
                .content("첫째 게시물 내용")
                .board(dummyBoard1)
                .viewCount(0L)
                .replyList(new ArrayList<>())
                .likeList(new ArrayList<>())
                .build());

        Post secondPost = postRepository.save(Post.builder()
                .member(dummyMember)
                .title("둘째 게시물 제목")
                .content("둘째 게시물 내용")
                .board(dummyBoard1)
                .viewCount(0L)
                .replyList(new ArrayList<>())
                .likeList(new ArrayList<>())
                .build());

        Member firstMember = memberRepository.save(Member.builder()
                .loginId("first123")
                .email("first@naver.com")
                .password("123123")
                .name("첫째임")
                .build());

        Member secondMember = memberRepository.save(Member.builder()
                .loginId("second2")
                .email("second@naver.com")
                .password("123123")
                .name("둘째다")
                .build());

        long firstPostId = firstPost.getId();
        long secondPostId = secondPost.getId();
        long dummyMemberId = dummyMember.getId();
        long firstMemberId = firstMember.getId();
        long secondMemberid = secondMember.getId();

        likeService.like(dummyMemberId, firstPostId, LikeType.POST);
        likeService.like(firstMemberId, firstPostId, LikeType.POST);
        likeService.like(secondMemberid, firstPostId, LikeType.POST);

        likeService.like(dummyMemberId, secondPostId, LikeType.POST);

        // when
        mockMvc.perform(get(BASE_URL + "/top")
                .param("post-count", Integer.toString(3)))
                .andDo(print());

        // then
    }

    private void setJwtInformation(long memberId) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> "ROLE_USER");

        UserDetails principal = new User(Long.toString(memberId), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}