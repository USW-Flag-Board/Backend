package com.FlagHome.backend.domain.search;

import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.repository.CategoryRepository;
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
    private CategoryRepository categoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private MockMvc mockMvc;

    private SearchService searchService;

    @Test
    @DisplayName("전체 게시판 조회 테스트")
    public void allBoardSearchTest() {
        // given
        searchService = new BoardSearchService(postRepository, categoryRepository);

        Member member = memberRepository.save(Member.builder().email("gildong@naver.com").name("홍길동").loginId("gildong12").password("123123").build());

        String firstTitle = "첫번째 제목";
        String secondTitle = "두번째 제목";

        Category category = categoryRepository.save(Category.builder().englishName("free").koreanName("자유").postList(new ArrayList<>()).build());
        Post post = postRepository.save(Post.builder().member(member).category(category).title(firstTitle).content("라라라").replyList(new ArrayList<>()).build());
        category.getPostList().add(post);

        Category category2 = categoryRepository.save(Category.builder().englishName("notice").koreanName("공지").postList(new ArrayList<>()).build());
        Post post2 = postRepository.save(Post.builder().member(member).category(category2).title(secondTitle).content("두두두").replyList(new ArrayList<>()).build());
        category2.getPostList().add(post2);

        long firstCategoryId = category.getId();
        long secondCategoryId = category2.getId();

        // when
        List<PostDto> result = null;
        if(searchService.getClass().equals(BoardSearchService.class))
            result = ((BoardSearchService)searchService).getAll();

        // then
        PostDto firstPostDto = result.get(0);
        assertThat(firstPostDto.getTitle()).isEqualTo(firstTitle);
        assertThat(firstPostDto.getCategoryId()).isEqualTo(firstCategoryId);

        PostDto secondPostDto = result.get(1);
        assertThat(secondPostDto.getTitle()).isEqualTo(secondTitle);
        assertThat(secondPostDto.getCategoryId()).isEqualTo(secondCategoryId);
    }

    @Test
    @DisplayName("카테고리를 이용한 게시판 조회 테스트")
    public void boardSearchWithCategoryTest() throws Exception {
        // given
        searchService = new BoardSearchService(postRepository, categoryRepository);

        Member member = memberRepository.save(Member.builder().email("gildong@naver.com").name("홍길동").loginId("gildong12").password("123123").build());

        String firstTitle = "첫번째 제목";
        String secondTitle = "두번째 제목";

        Category category = categoryRepository.save(Category.builder().englishName("free").koreanName("자유").postList(new ArrayList<>()).build());
        Post post = postRepository.save(Post.builder().member(member).category(category).title(firstTitle).content("라라라").replyList(new ArrayList<>()).build());
        category.getPostList().add(post);

        Category category2 = categoryRepository.save(Category.builder().englishName("notice").koreanName("공지").postList(new ArrayList<>()).build());
        Post post2 = postRepository.save(Post.builder().member(member).category(category2).title(secondTitle).content("두두두").replyList(new ArrayList<>()).build());
        category2.getPostList().add(post2);

        // when
        List<PostDto> result = searchService.getWithCategory("free");

        // then
        assertThat(result.size()).isEqualTo(1);
    }
}
