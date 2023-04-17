package com.FlagHome.backend.domain.post;

import com.FlagHome.backend.common.RepositoryTest;
import com.FlagHome.backend.domain.member.entity.Avatar;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.controller.dto.PostResponse;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.entity.enums.TopPostCondition;
import com.FlagHome.backend.domain.post.like.entity.PostLike;
import com.FlagHome.backend.domain.post.like.entity.ReplyLike;
import com.FlagHome.backend.domain.post.like.repository.LikeRepository;
import com.FlagHome.backend.domain.post.reply.entity.Reply;
import com.FlagHome.backend.domain.post.reply.repository.ReplyRepository;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class PostRepositoryTest extends RepositoryTest {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    private Post post;

    @BeforeEach
    void testSetup() {
        final String nickname = "john";
        Avatar avatar = Avatar.builder().nickname(nickname).build();
        member = memberRepository.save(Member.builder().avatar(avatar).build());
        post = postRepository.save(Post.builder().member(member).build());
    }

    @Test
    void 좋아요_체크_테스트() {
        // given
        final String content = "content";
        Reply reply = replyRepository.save(Reply.of(member, post, content));

        likeRepository.save(PostLike.of(member, post));
        likeRepository.save(ReplyLike.of(member, reply));

        // when
        boolean postLiked = likeRepository.isPostLiked(member.getId(), post.getId());
        boolean replyLiked = likeRepository.isPostLiked(member.getId(), reply.getId());

        // then
        assertThat(postLiked).isTrue();
        assertThat(replyLiked).isTrue();
    }

    @Nested
    class 탑_게시글_가져오기_테스트 {
        @Test
        void 핫게시글_가져오기_테스트() {
            // given
            final TopPostCondition condition = TopPostCondition.like;
            final int hotPostLikeCount = 5;

            Post notHotPost = postRepository.save(Post.builder().member(member).build());

            for (int i = 0; i < hotPostLikeCount ; i++) {
                post.increaseLikeCount();
                if (i > 2) {
                    notHotPost.increaseLikeCount();
                }
            }

            // when
            List<PostResponse> responses = postRepository.getTopFiveByCondition(condition);

            // then
            assertThat(responses.size()).isEqualTo(1);
            assertThat(responses.get(0).getLikeCount()).isEqualTo(hotPostLikeCount);
        }

        @Test
        void 최신게시글_가져오기_테스트() {
            // given
            final TopPostCondition condition = TopPostCondition.latest;

            Post post2 = postRepository.save(Post.builder().member(member).build());
            Post post3 = postRepository.save(Post.builder().member(member).build());

            // when
            List<PostResponse> responses = postRepository.getTopFiveByCondition(condition);

            // then
            assertThat(responses.size()).isEqualTo(3);
        }
    }
}
