package com.Flaground.backend.module.post.image;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.post.domain.Image;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.repository.ImageRepository;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class ImageRepositoryTest extends RepositoryTest {
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private PostRepository postRepository;

    @Nested
    class 게시글_번호_이미지키_가져오기_테스트 {
        @Test
        void 가져오기_성공_테스트() {
            // given
            final String key = "test";
            Post post = postRepository.save(Post.builder().build());
            imageRepository.save(new Image(post.getId(), key));

            // when
            List<String> keys = imageRepository.getKeysByPostId(post.getId());

            // then
            assertThat(keys).isNotNull();
            assertThat(keys.size()).isEqualTo(1);
            assertThat(keys.get(0)).isEqualTo(key);
        }

        @Test
        void 빈값_가져오기_테스트() {
            // given
            Post post = postRepository.save(Post.builder().build());

            // when
            List<String> keys = imageRepository.getKeysByPostId(post.getId());

            // then
            assertThat(keys).isNotNull();
            assertThat(keys).isEmpty();
        }
    }

    @Test
    void 키로_이미지_삭제_테스트() {
        // given
        final Long postId = 1L;
        final String test = "key";
        List<String> keys = IntStream.range(0, 10)
                .mapToObj(i -> test + i)
                .toList();

        List<Image> images = keys.stream()
                .map(key -> new Image(postId, key))
                .toList();

        imageRepository.saveAll(images);

        // when
        imageRepository.deleteImagesByKeys(keys);

        // then
        List<Image> findImages = imageRepository.findAll();
        assertThat(findImages).isEmpty();
    }
}
