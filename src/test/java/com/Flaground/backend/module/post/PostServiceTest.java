package com.Flaground.backend.module.post;

import com.Flaground.backend.infra.aws.s3.service.AwsS3Service;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepository;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.post.domain.Image;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.PostData;
import com.Flaground.backend.module.post.domain.repository.ImageRepository;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import com.Flaground.backend.module.post.service.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
class PostServiceTest {
    @Autowired
    public PostService postService;

    @Autowired
    public MemberRepository memberRepository;

    @Autowired
    public PostRepository postRepository;

    @Autowired
    public BoardRepository boardRepository;

    @Autowired
    public ImageRepository imageRepository;

    @Autowired
    public AwsS3Service awsS3Service;

    private Member member;

    private Board board;

    private static final List<String> deleteKeys = List.of("key3", "key4");

    @BeforeEach
    void testSetup() {
        final String name = "hejow";
        final String email = "gmlwh124@suwon.ac.kr";
        final String boardName = "자유 게시판";
        final BoardType boardType = BoardType.MAIN;

        member = memberRepository.save(Member.builder().name(name).email(email).build());
        board = boardRepository.save(Board.builder().name(boardName).boardType(boardType).build());
    }

    @Test
    void 게시글_생성_테스트() {
        // given
        final String title = "title";
        final String content = "content";
        final List<String> saveKeys = List.of("key1", "key2");

        PostData postData = PostData.builder()
                .title(title)
                .content(content)
                .boardName(board.getName())
                .saveImages(saveKeys)
                .deleteImages(deleteKeys)
                .build();

        // when
        Long postId = postService.create(member.getId(), postData);

        // then
        Post savedPost = postRepository.findById(postId).orElse(null);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getTitle()).isEqualTo(title);
        assertThat(savedPost.getContent()).isEqualTo(content);
        assertThat(savedPost.getBoardName()).isEqualTo(board.getName());

        List<Image> images = imageRepository.findAll();
        assertThat(images.size()).isEqualTo(2);

        assertThat(awsS3Service.size()).isEqualTo(0);
    }

    @Test
    void 게시글_수정_테스트() {
        // given
        final String newTitle = "newTitle";
        final String newContent = "newContent";
        final String savedKey = "key5";
        final List<String> saveKeys = List.of("key1", "key2");

        Post post = postRepository.save(Post.builder().member(member).build());
        imageRepository.save(new Image(post.getId(), savedKey));

        PostData postData = PostData.builder()
                .title(newTitle)
                .content(newContent)
                .boardName(board.getName())
                .saveImages(saveKeys)
                .deleteImages(deleteKeys)
                .build();

        // when
        postService.update(member.getId(), post.getId(), postData);

        // then
        Post updatedPost = postRepository.findById(post.getId()).orElse(null);
        assertThat(updatedPost).isNotNull();
        assertThat(updatedPost.getTitle()).isEqualTo(newTitle);
        assertThat(updatedPost.getContent()).isEqualTo(newContent);
        assertThat(updatedPost.getBoardName()).isEqualTo(board.getName());
        assertThat(updatedPost.isEdited()).isTrue();

        List<Image> images = imageRepository.findAll();
        assertThat(images.size()).isEqualTo(2);

        assertThat(awsS3Service.size()).isEqualTo(0);
    }

    @Test
    void test() {
        List<String> list = new ArrayList<>();
        System.out.println("list = " + list);

        List<String> converted = list.stream()
                .map(n -> n + 1)
                .toList();

        System.out.println("converted = " + converted);
    }

    @TestConfiguration
    static class CustomConfig {
        private static final List<String> s3Storage = new ArrayList<>(deleteKeys);

        @Bean
        @Primary
        public CustomS3Service customS3Service() {
            return new CustomS3Service(s3Storage);
        }

        static class CustomS3Service implements AwsS3Service {
            private final List<String> s3Storage;

            public CustomS3Service(List<String> s3Storage) {
                this.s3Storage = s3Storage;
            }

            @Override
            public String upload(MultipartFile file, String directory) {
                return null;
            }
            @Override
            public void delete(String key) {

            }
            @Override
            public void deleteAll(List<String> keys) {
                for (String key : keys) {
                    System.out.println("delete query : " + key);
                    s3Storage.remove(key);
                }
            }
            @Override
            public int size() {
                return s3Storage.size();
            }
        }
    }
}
