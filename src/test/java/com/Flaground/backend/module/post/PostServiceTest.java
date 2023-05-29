package com.Flaground.backend.module.post;

import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepository;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.post.controller.dto.request.PostRequest;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.controller.mapper.PostMapper;
import com.Flaground.backend.module.post.domain.PostData;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import com.Flaground.backend.module.post.service.PostService;
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

        PostData data = postMapper.toMetaData(request);
        Long memberId = memberRepository.findByEmail(email).get().getId();

        // when
        Long postId = postService.create(memberId, data);

        // then
        Post savedPost = postRepository.findById(postId).get();
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
        assertThat(savedPost.getBoardName()).isEqualTo(boardName);
    }
}
