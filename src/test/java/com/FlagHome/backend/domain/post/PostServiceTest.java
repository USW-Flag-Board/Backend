package com.FlagHome.backend.domain.post;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.entity.enums.BoardType;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.controller.dto.CreatePostRequest;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.mapper.PostMapper;
import com.FlagHome.backend.domain.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PostServiceTest {
    @Autowired
    public PostService postService;

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public PostMapper postMapper;

    @Autowired
    public BoardRepository boardRepository;

    private Member member;

    private Board board;

    @BeforeEach
    void init() {
        final String name = "hejow";
        final String email = "gmlwh124@suwon.ac.kr";

        memberRepository.save(Member.builder()
                .name(name)
                .email(email)
                .build());

        final String boardName = "자유 게시판";
        final BoardType boardType = BoardType.MAIN;

        boardRepository.saveAndFlush(Board.builder()
                .name(boardName)
                .boardType(boardType)
                .build());
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    void createPostTest() {
        // given
        final String email = "gmlwh124@suwon.ac.kr";
        final String title = "title";
        final String content = "content";
        final String boardName = "자유 게시판";

        CreatePostRequest request = CreatePostRequest.builder()
                .title(title)
                .content(content)
                .boardName(boardName)
                .build();

        Post post = postMapper.CreateRequestToEntity(request);
        Long memberId = memberRepository.findByEmail(email).get().getId();

        // when
        Post savedPost = postService.create(memberId, post, boardName);

        // then
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
        assertThat(savedPost.getBoard().getName()).isEqualTo(boardName);
    }
}
