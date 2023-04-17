package com.FlagHome.backend.domain.post;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.entity.enums.BoardType;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.controller.dto.request.PostRequest;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.mapper.PostMapper;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class PostServiceTest {
    @Autowired
    public PostService postService;

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public PostMapper postMapper;

    @Autowired
    public BoardRepository boardRepository;

    private Member member;

    private Board board;

    @BeforeEach
    public void testSetup() {
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
    public void 게시글_생성_테스트() {
        // given
        final String email = "gmlwh124@suwon.ac.kr";
        final String title = "title";
        final String content = "content";
        final String boardName = "자유 게시판";

        PostRequest request = PostRequest.builder()
                .title(title)
                .content(content)
                .boardName(boardName)
                .build();

        Post post = postMapper.toEntity(request);
        Long memberId = memberRepository.findByEmail(email).get().getId();

        // when
        Long postId = postService.createPost(memberId, post, boardName);

        // then
        Post savedPost = postRepository.findById(postId).get();
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
        assertThat(savedPost.getBoard().getName()).isEqualTo(boardName);
    }
}
