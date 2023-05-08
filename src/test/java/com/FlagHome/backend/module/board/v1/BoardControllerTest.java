//package com.FlagHome.backend.module.board.controller;
//
//import com.FlagHome.backend.module.board.repository.BoardRepository;
//import com.FlagHome.backend.module.board.service.BoardService;
//import com.FlagHome.backend.module.member.domain.repository.MemberRepository;
//import com.FlagHome.backend.module.post.domain.repository.PostRepository;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.web.context.WebApplicationContext;
//
//@SpringBootTest
//@AutoConfigureMockMvc
//@WithMockUser
//public class BoardControllerTest {
//    private final static String BASE_URI = "/boards";
//
//    @Autowired
//    private BoardService boardService;
//    @Autowired
//    private PostRepository postRepository;
//    @Autowired
//    private BoardRepository boardRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private WebApplicationContext webApplicationContext;
//    @Autowired
//    private MockMvc mockMvc;
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @BeforeEach
//    public void testSetting() {
//        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
//                .addFilter(new CharacterEncodingFilter("UTF-8", true))
//                .build();
//    }
//
//    @Test
//    @DisplayName("카테고리를 이용한 게시판 조회 테스트")
//    public void boardSearchWithCategoryTest() throws Exception {
//        // given
//        Member member = memberRepository.save(Member.builder().email("gildong@naver.com").name("홍길동").loginId("gildong12").password("123123").build());
//
//        String firstTitle = "첫번째 제목";
//        String secondTitle = "두번째 제목";
//
//        Board board = boardRepository.save(Board.builder().englishName("free").koreanName("자유").build());
//
//        postRepository.save(Post.builder().member(member).board(board).title(firstTitle).content("라라라").replyList(new ArrayList<>()).build());
//        postRepository.save(Post.builder().member(member).board(board).title(secondTitle).content("두두두").replyList(new ArrayList<>()).build());
//
//        // when
//        ResultActions actions = mockMvc.perform(get(BASE_URI)
//                .with(csrf())
//                .param("name", "free"));
//
//        // then
//        actions
//                .andDo(print())
//                .andExpect(status().isOk());
//
//        MvcResult result = actions.andReturn();
//        String jsonResult = result.getResponse().getContentAsString();
//        List<PostDto> postDtoList = objectMapper.readValue(jsonResult, new TypeReference<>() {});
//        assertThat(postDtoList.size()).isEqualTo(2);
//    }
//}
