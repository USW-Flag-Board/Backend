package com.FlagHome.backend.domain.post.controller;

import com.FlagHome.backend.domain.category.entity.Category;
import com.FlagHome.backend.domain.category.repository.CategoryRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
@WithMockUser
class PostControllerTest {

    private final String baseUrl = "/api/post";

    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    private Member dummyMember;

    private Category dummyCategory1, dummyCategory2;


    @BeforeEach
    public void testSetting() {
        dummyMember = memberRepository.save(Member.builder()
                .loginId("gildong11")
                .password("123123")
                .email("gildong@naver.com")
                .studentId("2").build());
        dummyCategory1 = categoryRepository.save(Category.builder()
                        .koreanName("일반게시판")
                        .englishName("board")
                        .categoryDepth(0L)
                .build());
        dummyCategory2 = categoryRepository.save(Category.builder()
                .koreanName("활동")
                .englishName("activity")
                .categoryDepth(1L)
                .parent(dummyCategory1)
                .build());
    }

    @Test
    @DisplayName("게시글 생성 테스트")
    public void createPostTest() throws Exception {
        String title = "테스트 제목";
        String content = "테스트 내용";

        PostDto postDto = new PostDto();
        postDto.setTitle(title);
        postDto.setContent(content);
        postDto.setUserId(dummyMember.getId());
        postDto.setCategoryId(dummyCategory1.getId());

        mockMvc.perform(post(baseUrl)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postDto)))
                .andDo(print());

        List<Post> postList = postRepository.findAll();
        assert postList.size() == 1;

        Post createdPost = postList.get(0);
        assert createdPost.getTitle().equals(title);
        assert createdPost.getContent().equals(content);
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
                .member(dummyMember)
                .category(dummyCategory2)
                .build());

        mockMvc.perform(get(baseUrl + "?postId=" + postEntity.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("title", is(title)))
                .andExpect(jsonPath("content", is(content)))
                .andDo(print());
    }

    @Test
    @DisplayName("게시글 수정 테스트")
    public void updatePostTest() throws Exception {
        String originalTitle = "원래제목";
        String originalContent = "원래내용";
        Category originalCategory = dummyCategory1;

        Post postEntity = postRepository.save(Post.builder()
                                        .title(originalTitle)
                                        .content(originalContent)
                                        .category(originalCategory)
                                        .viewCount(0L)
                                        .replyList(new ArrayList<>())
                                        .member(dummyMember)
                                        .build());

        String changedTitle = "바뀐제목";
        String changedContent = "바뀐내용";

        Category changedCategory = dummyCategory2;

        PostDto changedPostDto = new PostDto(postEntity);
        changedPostDto.setTitle(changedTitle);
        changedPostDto.setContent(changedContent);
        changedPostDto.setCategoryId(changedCategory.getId());

        String jsonBody = objectMapper.writeValueAsString(changedPostDto);

        mockMvc.perform(patch(baseUrl)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonBody))
                .andExpect(status().isOk())
                .andDo(print());

        Post changedPostEntity = postRepository.findById(changedPostDto.getId()).orElse(null);
        assert changedPostEntity != null;
        assert changedPostEntity.getTitle().equals(changedTitle);
        assert changedPostEntity.getContent().equals(changedContent);
        assert changedPostEntity.getCategory().equals(changedCategory);
    }

    @Test
    @DisplayName("게시글 삭제 테스트")
    public void deletePostTest() throws Exception {
        Post postEntity = postRepository.save(Post.builder()
                        .title("삭제될 게시글 제목")
                        .content("삭제될 게시글 내용")
                        .category(dummyCategory2)
                        .member(dummyMember)
                        .build());

        long deleteTargetPostId = postEntity.getId();

        mockMvc.perform(delete(baseUrl + "/" + deleteTargetPostId))
                .andDo(print());

        Post post = postRepository.findById(deleteTargetPostId).orElse(null);
        assert post == null;
    }
}