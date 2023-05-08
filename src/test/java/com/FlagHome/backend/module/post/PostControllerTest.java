package com.FlagHome.backend.module.post;

import com.FlagHome.backend.common.IntegrationTest;
import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import com.FlagHome.backend.module.board.entity.Board;
import com.FlagHome.backend.module.board.entity.enums.BoardType;
import com.FlagHome.backend.module.board.repository.BoardRepository;
import com.FlagHome.backend.module.member.domain.Avatar;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.domain.enums.Role;
import com.FlagHome.backend.module.member.domain.repository.MemberRepository;
import com.FlagHome.backend.module.post.controller.dto.request.PostRequest;
import com.FlagHome.backend.module.post.controller.dto.request.SearchRequest;
import com.FlagHome.backend.module.post.domain.Like;
import com.FlagHome.backend.module.post.domain.Post;
import com.FlagHome.backend.module.post.domain.Reply;
import com.FlagHome.backend.module.post.domain.enums.PostStatus;
import com.FlagHome.backend.module.post.domain.enums.SearchOption;
import com.FlagHome.backend.module.post.domain.enums.SearchPeriod;
import com.FlagHome.backend.module.post.domain.repository.LikeRepository;
import com.FlagHome.backend.module.post.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class PostControllerTest extends IntegrationTest {
    private static final String BASE_URI = "/posts";

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PostRepository  postRepository;

    @Autowired
    private LikeRepository likeRepository;

    private Member member;

    private Board board;

    @BeforeEach
    void setUp() {
        final String email = "gmlwh124@suwon.ac.kr";
        final String nickname = "john";
        final Role role = Role.ROLE_USER;

        Avatar avatar = Avatar.builder().nickname(nickname).build();

        member = memberRepository.save(Member.builder()
                .email(email)
                .avatar(avatar)
                .role(role)
                .build());

        board = boardRepository.save(Board.builder().boardType(BoardType.MAIN).name("자유게시판").build());

        setSecurityContext(member);
    }

    @Nested
    class 게시글_가져오기_테스트 {
        private final String title = "title";
        private final String content = "content";
        private final String boardName = "자유게시판";
        private Post post;
        private String uri;

        @BeforeEach
        void setup() {
            post = postRepository.save(Post.builder()
                    .member(member)
                    .boardName(boardName)
                    .title(title)
                    .content(content)
                    .build());

            uri = BASE_URI + "/" + post.getId();
        }

        @Test
        public void 비회원_게시글_가져오기_성공() throws Exception {
            // given
            final String reply = "reply";
            final int viewCount = post.getViewCount();
            post.addReply(Reply.of(member, post.getId(), reply));
            clearSecurityContext();

            // when
            mockMvc.perform(get(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.postDetail.title", is(title)))
                    .andExpect(jsonPath("$.payload.postDetail.content", is(content)))
                    .andExpect(jsonPath("$.payload.postDetail.like.liked", is(false)))
                    .andExpect(jsonPath("$.payload.postDetail.like.likeCount", is(0)))
                    .andExpect(jsonPath("$.payload.replies[0].content", is(reply)))
                    .andExpect(jsonPath("$.payload.replies[0].like.liked", is(false)))
                    .andExpect(jsonPath("$.payload.replies[0].like.likeCount", is(0)))
                    .andDo(print());

            // then
            Post findPost = postRepository.findById(post.getId()).get();
            assertThat(findPost.getViewCount()).isEqualTo(viewCount + 1);
        }

        @Test
        public void 회원_게시글_가져오기_성공() throws Exception {
            // given
            final String reply = "reply";
            final int viewCount = post.getViewCount();
            post.addReply(Reply.of(member, post.getId(), reply));

            // when
            mockMvc.perform(get(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.postDetail.title", is(title)))
                    .andExpect(jsonPath("$.payload.postDetail.content", is(content)))
                    .andExpect(jsonPath("$.payload.postDetail.like.liked", is(false)))
                    .andExpect(jsonPath("$.payload.postDetail.like.likeCount", is(0)))
                    .andExpect(jsonPath("$.payload.replies[0].content", is(reply)))
                    .andExpect(jsonPath("$.payload.replies[0].like.liked", is(false)))
                    .andExpect(jsonPath("$.payload.replies[0].like.likeCount", is(0)))
                    .andDo(print());

            // then
            Post findPost = postRepository.findById(post.getId()).get();
            assertThat(findPost.getViewCount()).isEqualTo(viewCount + 1);
        }

        @Test
        void 게시글_가져오기_실패() throws Exception {
            // given
            post.delete();

            // when
            ResultActions resultActions = mockMvc.perform(get(uri)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.INACCESSIBLE_POST.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.INACCESSIBLE_POST.getMessage()))
                    .andDo(print());
        }
    }

    @Test
    void 게시글_생성_테스트() throws Exception {
        // given
        final String title = "title";
        final String content = "content";

        PostRequest request = PostRequest.builder()
                .title(title)
                .content(content)
                .boardName(board.getName())
                .build();

        // when
        mockMvc.perform(post(BASE_URI)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andDo(print());

        // then
        Post post = postRepository.findAll().get(0);
        assertThat(post.getMember().getId()).isEqualTo(member.getId());
        assertThat(post.getTitle()).isEqualTo(title);
        assertThat(post.getContent()).isEqualTo(content);
        assertThat(post.getViewCount()).isEqualTo(0);
    }

    @Nested
    class 게시글_수정_테스트 {
        private PostRequest request;

        @BeforeEach
        void setup() {
            final String title = "title";
            final String content = "content";
            final String boardName = board.getName();

            request = PostRequest.builder()
                    .title(title)
                    .content(content)
                    .boardName(boardName)
                    .build();
        }

        @Test
        void 게시글_수정_성공() throws Exception {
            // given
            final String oldTitle = "old";

            Post post = postRepository.save(Post.builder().member(member).title(oldTitle).build());
            String uri = BASE_URI + "/" + post.getId();

            // when
            mockMvc.perform(put(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andDo(print());

            // then
            Post updatedPost = postRepository.findById(post.getId()).get();
            assertThat(updatedPost.getTitle()).isNotEqualTo(oldTitle);
            assertThat(updatedPost.isEdited()).isTrue();
        }

        @Test
        void 게시글_수정_실패() throws Exception {
            // given
            Member newMember = memberRepository.save(Member.builder().build());
            Post post = postRepository.save(Post.builder().member(newMember).build());
            String uri = BASE_URI + "/" + post.getId();

            // when
            ResultActions resultActions = mockMvc.perform(put(uri)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)));

            // then
            resultActions.andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.NOT_AUTHOR.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.NOT_AUTHOR.getMessage()))
                    .andDo(print());
        }
    }

    @Nested
    class 게시글_삭제_테스트 {
        @Test
        void 게시글_삭제_성공() throws Exception {
            // given
            Post post = postRepository.save(Post.builder().member(member).build());
            String uri = BASE_URI + "/" + post.getId();

            // when
            mockMvc.perform(patch(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andDo(print());

            // then
            Post deletedPost = postRepository.findById(post.getId()).get();
            assertThat(deletedPost.getStatus()).isEqualTo(PostStatus.DELETED);
            assertThatExceptionOfType(CustomException.class)
                    .isThrownBy(deletedPost::isAccessible)
                    .withMessage(ErrorCode.INACCESSIBLE_POST.getMessage());
        }

        @Test
        void 게시글_삭제_실패() throws Exception {
            // given
            Member anotherMember = memberRepository.save(Member.builder().build());
            Post post = postRepository.save(Post.builder().member(anotherMember).build());
            String uri = BASE_URI + "/" + post.getId();

            // when
            ResultActions resultActions = mockMvc.perform(patch(uri)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.NOT_AUTHOR.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.NOT_AUTHOR.getMessage()))
                    .andDo(print());
        }
    }

    @Nested
    class 게시글_좋아요_테스트 {
        private Post post;
        private String uri;

        @BeforeEach
        void setup() {
            post = postRepository.save(Post.builder().member(member).build());
            uri = BASE_URI + "/" + post.getId() + "/like";
        }

        @Test
        void 게시글_좋아요_성공() throws Exception {
            // given
            final int likesAtFirst = post.getLikeCount();

            // when
            mockMvc.perform(post(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isCreated())
                    .andExpect(jsonPath("$.payload.liked", is(true)))
                    .andExpect(jsonPath("$.payload.likeCount", is(likesAtFirst + 1)))
                    .andDo(print());

            // then
            Post findPost = postRepository.findById(post.getId()).get();
            assertThat(findPost.getLikeCount()).isEqualTo(likesAtFirst + 1);
            boolean isLiked = likeRepository.existsByIds(member.getId(), post.getId());
            assertThat(isLiked).isTrue();
        }

        @Test
        void 게시글_좋아요_실패() throws Exception {
            // given
            likeRepository.save(Like.from(member.getId(), post.getId()));

            // when
            ResultActions resultActions = mockMvc.perform(post(uri)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isConflict())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.ALREADY_LIKED.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.ALREADY_LIKED.getMessage()))
                    .andDo(print());
        }

        @Test
        void 게시글_좋아요_취소_성공() throws Exception {
            // given
            likeRepository.save(Like.from(member.getId(), post.getId()));
            final int likeCountAtFirst = post.getLikeCount();

            // when
            mockMvc.perform(delete(uri)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.payload.liked", is(false)))
                    .andExpect(jsonPath("$.payload.likeCount", is(likeCountAtFirst - 1)))
                    .andDo(print());

            // then
            Post findPost = postRepository.findById(post.getId()).get();
            assertThat(findPost.getLikeCount()).isEqualTo(likeCountAtFirst - 1);
            boolean shouldBeFalse = likeRepository.existsByIds(member.getId(), post.getId());
            assertThat(shouldBeFalse).isFalse();
        }

        @Test
        void 게시글_좋아요_취소_실패() throws Exception {
            // given

            // when
            ResultActions resultActions = mockMvc.perform(delete(uri)
                    .contentType(MediaType.APPLICATION_JSON));

            // then
            resultActions
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("errorCode").value(ErrorCode.NEVER_LIKED.toString()))
                    .andExpect(jsonPath("message").value(ErrorCode.NEVER_LIKED.getMessage()))
                    .andDo(print());
        }
    }

    @Test
    public void 게시판_검색_테스트() throws Exception {
        // given
        final String boardName = "자유게시판";
        final String keyword = "test";

        Post post = Post.builder().title("test").build();
        postRepository.save(Post.of(member, boardName, post));

        SearchRequest request = SearchRequest.builder()
                .board(boardName)
                .keyword(keyword)
                .option(SearchOption.title)
                .period(SearchPeriod.all)
                .build();

        final String uri = BASE_URI + "/search";

        // when
        ResultActions resultActions = mockMvc.perform(get(uri)
                .param("board", request.getBoard())
                .param("keyword", request.getKeyword())
                .param("period", String.valueOf(request.getPeriod()))
                .param("option", String.valueOf(request.getOption()))
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.resultCount", is(1)))
                .andExpect(jsonPath("$.payload.searchResults[0].title", is(keyword)))
                .andDo(print());
    }

    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }

    private void clearSecurityContext() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}
