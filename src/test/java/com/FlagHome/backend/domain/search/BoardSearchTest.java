package com.FlagHome.backend.domain.search;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.search.service.SearchService;
import com.FlagHome.backend.domain.search.service.impl.BoardSearchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
public class BoardSearchTest {
    private final String baseURI = "/api/board";

    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;

    private SearchService searchService;

    @Test
    @DisplayName("전체 게시판 조회 테스트")
    public void allBoardSearchTest() {
        // given
        searchService = new BoardSearchService(postRepository, boardRepository);

        Member member = memberRepository.save(Member.builder().email("gildong@naver.com").name("홍길동").loginId("gildong12").password("123123").build());

        String firstTitle = "첫번째 제목";
        String secondTitle = "두번째 제목";

        Board board = boardRepository.save(Board.builder().englishName("free").koreanName("자유").postList(new ArrayList<>()).build());
        Post post = postRepository.save(Post.builder().member(member).board(board).title(firstTitle).content("라라라").replyList(new ArrayList<>()).build());
        board.getPostList().add(post);

        Board board2 = boardRepository.save(Board.builder().englishName("notice").koreanName("공지").postList(new ArrayList<>()).build());
        Post post2 = postRepository.save(Post.builder().member(member).board(board2).title(secondTitle).content("두두두").replyList(new ArrayList<>()).build());
        board2.getPostList().add(post2);

        long firstCategoryId = board.getId();
        long secondCategoryId = board2.getId();

        // when
        List<PostDto> result = null;
        if(searchService.getClass().equals(BoardSearchService.class))
            result = ((BoardSearchService)searchService).getAll();

        // then
        PostDto firstPostDto = result.get(0);
        assertThat(firstPostDto.getTitle()).isEqualTo(firstTitle);
        assertThat(firstPostDto.getBoardId()).isEqualTo(firstCategoryId);

        PostDto secondPostDto = result.get(1);
        assertThat(secondPostDto.getTitle()).isEqualTo(secondTitle);
        assertThat(secondPostDto.getBoardId()).isEqualTo(secondCategoryId);
    }

    @Test
    @DisplayName("카테고리를 이용한 게시판 조회 테스트")
    public void boardSearchWithCategoryTest() throws Exception {
        // given
        searchService = new BoardSearchService(postRepository, boardRepository);

        Member member = memberRepository.save(Member.builder().email("gildong@naver.com").name("홍길동").loginId("gildong12").password("123123").build());

        String firstTitle = "첫번째 제목";
        String secondTitle = "두번째 제목";

        Board board = boardRepository.save(Board.builder().englishName("free").koreanName("자유").postList(new ArrayList<>()).build());
        Post post = postRepository.save(Post.builder().member(member).board(board).title(firstTitle).content("라라라").replyList(new ArrayList<>()).build());
        board.getPostList().add(post);

        Board board2 = boardRepository.save(Board.builder().englishName("notice").koreanName("공지").postList(new ArrayList<>()).build());
        Post post2 = postRepository.save(Post.builder().member(member).board(board2).title(secondTitle).content("두두두").replyList(new ArrayList<>()).build());
        board2.getPostList().add(post2);

        // when
        List<PostDto> result = searchService.getWithCategory("free");

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}
