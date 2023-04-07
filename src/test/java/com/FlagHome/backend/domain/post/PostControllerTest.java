package com.FlagHome.backend.domain.post;

import com.FlagHome.backend.common.IntegrationTest;
import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.entity.enums.BoardType;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.member.entity.Avatar;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.entity.enums.Role;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.controller.dto.PostRequest;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.entity.PostStatus;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.global.exception.ErrorCode;
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

        board = boardRepository.save(Board.builder().boardType(BoardType.MAIN).name("자유 게시판").build());

        setSecurityContext(member);
    }

    @Nested
    class 게시글_가져오기_테스트 {
        private Post post;
        private String uri;

        @BeforeEach
        void setup() {
            final String title = "title";
            final String content = "content";

            post = postRepository.save(Post.builder().member(member).board(board).title(title).content(content).build());
            uri = BASE_URI + "/" + post.getId();
        }

        @Test
        void 게시글_가져오기_성공() throws Exception {
            // given
            int viewCount = post.getViewCount();

            // when
            mockMvc.perform(get(uri)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
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
            ResultActions resultActions = mockMvc.perform(get(uri).contentType(MediaType.APPLICATION_JSON));

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
            assertThat(deletedPost.isNotAccessible()).isTrue();
            assertThat(deletedPost.getStatus()).isEqualTo(PostStatus.DELETED);
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

    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
