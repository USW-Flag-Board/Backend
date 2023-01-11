package com.FlagHome.backend.domain.board.controller;

import com.FlagHome.backend.domain.board.entity.Board;
import com.FlagHome.backend.domain.board.repository.BoardRepository;
import com.FlagHome.backend.domain.board.service.BoardService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@WithMockUser
public class BoardControllerTest {
    private final static String BASE_URI = "/api/board";

    @Autowired
    private BoardService boardService;
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private WebApplicationContext webApplicationContext;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void testSetting() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("UTF-8", true))
                .build();
    }

    @Test
    @DisplayName("카테고리를 이용한 게시판 조회 테스트")
    public void boardSearchWithCategoryTest() throws Exception {
        // given
        Member member = memberRepository.save(Member.builder().email("gildong@naver.com").name("홍길동").loginId("gildong12").password("123123").build());

        String firstTitle = "첫번째 제목";
        String secondTitle = "두번째 제목";

        Board board = boardRepository.save(Board.builder().englishName("free").koreanName("자유").build());

        postRepository.save(Post.builder().member(member).board(board).title(firstTitle).content("라라라").replyList(new ArrayList<>()).build());
        postRepository.save(Post.builder().member(member).board(board).title(secondTitle).content("두두두").replyList(new ArrayList<>()).build());

        // when
        ResultActions actions = mockMvc.perform(get(BASE_URI)
                .with(csrf())
                .param("name", "free"));

        // then
        actions
                .andDo(print())
                .andExpect(status().isOk());

        MvcResult result = actions.andReturn();
        String jsonResult = result.getResponse().getContentAsString();
        List<PostDto> postDtoList = objectMapper.readValue(jsonResult, new TypeReference<>() {});
        assertThat(postDtoList.size()).isEqualTo(2);
    }
}
