package com.FlagHome.backend.module.post;

import com.FlagHome.backend.common.RepositoryTest;
import com.FlagHome.backend.module.board.entity.Board;
import com.FlagHome.backend.module.board.entity.enums.BoardType;
import com.FlagHome.backend.module.board.repository.BoardRepository;
import com.FlagHome.backend.module.member.domain.Avatar;
import com.FlagHome.backend.module.member.domain.Member;
import com.FlagHome.backend.module.member.domain.repository.MemberRepository;
import com.FlagHome.backend.module.post.controller.dto.response.*;
import com.FlagHome.backend.module.post.domain.Post;
import com.FlagHome.backend.module.post.domain.Reply;
import com.FlagHome.backend.module.post.domain.enums.SearchOption;
import com.FlagHome.backend.module.post.domain.enums.SearchPeriod;
import com.FlagHome.backend.module.post.domain.enums.TopPostCondition;
import com.FlagHome.backend.module.post.domain.repository.LikeRepository;
import com.FlagHome.backend.module.post.domain.repository.PostRepository;
import com.FlagHome.backend.module.post.domain.repository.ReplyRepository;
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

    @Autowired
    private BoardRepository boardRepository;

    private Member member;

    private Post post;

    @BeforeEach
    void testSetup() {
        final String loginId = "gmlwh124";
        final String nickname = "john";
        Avatar avatar = Avatar.builder().nickname(nickname).build();
        member = memberRepository.save(Member.builder().loginId(loginId).avatar(avatar).build());
        post = postRepository.save(Post.builder().member(member).build());
    }

    @Test
    public void 게시글_상세보기_테스트() {
        // given
        final String loginId = "gmlwh124";
        final String nickname = "john";
        final String content = "content";

        post.addReply(Reply.of(member, post.getId(), content));
        post.addReply(Reply.of(member, post.getId(), content));

        // when
        GetPostResponse response = postRepository.getWithReplies(null, post.getId());

        // then
        PostDetailResponse detailResponse = response.getPostDetail();
        List<ReplyResponse> replyResponses = response.getReplies();

        assertThat(detailResponse.getLoginId()).isEqualTo(loginId);
        assertThat(detailResponse.getNickname()).isEqualTo(nickname);
        assertThat(detailResponse.getLike().isLiked()).isEqualTo(false);
        assertThat(detailResponse.getLike().getLikeCount()).isEqualTo(0);
        assertThat(replyResponses.size()).isEqualTo(2);
        assertThat(replyResponses.get(0).getContent()).isEqualTo(content);
        assertThat(replyResponses.get(0).getLoginId()).isEqualTo(loginId);
        assertThat(replyResponses.get(0).getNickname()).isEqualTo(nickname);
        assertThat(replyResponses.get(0).getLike().isLiked()).isEqualTo(false);
        assertThat(replyResponses.get(0).getLike().getLikeCount()).isEqualTo(0);
    }

    @Nested
    class 탑_게시글_가져오기_테스트 {
        @Test
        void 핫게시글_가져오기_테스트() {
            // given
            final TopPostCondition condition = TopPostCondition.like;
            final int hotPostLikeCount = 5;

            Post notHotPost = postRepository.save(Post.builder().member(member).build());

            for (int i = 1 ; i <= hotPostLikeCount; i++) {
                if (i > 3) notHotPost.like();
                post.like();
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

            postRepository.save(Post.builder().member(member).build());
            postRepository.save(Post.builder().member(member).build());

            // when
            List<PostResponse> responses = postRepository.getTopFiveByCondition(condition);

            // then
            assertThat(responses.size()).isEqualTo(3);
        }
    }

    @Nested
    class 게시글_검색_테스트 {
        private Board board;
        private final String boardName = "자유게시판";
        private final SearchPeriod period = SearchPeriod.all;

        @BeforeEach
        void setup() {
            board = boardRepository.save(Board.builder()
                    .boardType(BoardType.MAIN)
                    .name(boardName)
                    .build());
        }

        @Test
        void 게시글_제목_검색_테스트() {
            // given
            final String title = "test";
            final String title2 = "testst";
            final String title3 = "teest";

            Post post1 = Post.builder().boardName(boardName).member(member).title(title).build();
            Post post2 = Post.builder().boardName(boardName).member(member).title(title2).build();
            Post post3 = Post.builder().boardName(boardName).member(member).title(title3).build();

            postRepository.saveAll(List.of(post1, post2, post3));

            final String keyword = "test";
            final SearchOption option = SearchOption.title;

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(2);
        }

        @Test
        void 게시글_내용_검색_테스트() {
            // given
            final String content1 = "this is for test";
            final String content2 = "this istestfor";
            final String content3 = "this is for teest";

            Post post1 = Post.builder().boardName(boardName).member(member).content(content1).build();
            Post post2 = Post.builder().boardName(boardName).member(member).content(content2).build();
            Post post3 = Post.builder().boardName(boardName).member(member).content(content3).build();

            postRepository.saveAll(List.of(post1, post2, post3));

            final String keyword = "test";
            final SearchOption option = SearchOption.content;

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(2);
        }

        @Test
        void 게시글_댓글_검색_테스트() {
            // given
            final String content = "test";
            Post searchPost = postRepository.save(Post.of(member, boardName, post));

            Reply reply1 = Reply.of(member, post.getId(), content);
            Reply reply2 = Reply.of(member, post.getId(), content);

            replyRepository.saveAll(List.of(reply1, reply2));

            final String keyword = "test";
            final SearchOption option = SearchOption.reply;

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(1);
        }

        @Test
        void 게시글_내용_댓글_검색_테스트() {
            // given
            final String content = "test";
            final String content2 = "ttest";

            Post searchPost = Post.builder().member(member).boardName(boardName).content(content).build();
            Post searchPost2 = Post.builder().member(member).boardName(boardName).content(content2).build();

            Reply reply1 = Reply.of(member, post.getId(), content); // 검색X : 게시판 정보가 없음
            Reply reply2 = Reply.of(member, post.getId(), content2); // 중복
            Reply reply3 = Reply.of(member, post.getId(), content2); // 중복

            postRepository.saveAll(List.of(searchPost, searchPost2));
            replyRepository.saveAll(List.of(reply1, reply2, reply3));

            final String keyword = "test";
            final SearchOption option = SearchOption.content_and_reply;

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(2);
        }

        @Test
        void 게시글_작성자_검색_테스트() {
            // given
            postRepository.save(Post.builder().member(member).boardName(boardName).build());

            final String keyword = "john";
            final SearchOption option = SearchOption.author;

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(1);
        }
    }

    @Test
    void 게시글_통합_검색_테스트() {
        // given
        final String title = "is this test?";
        final String content = "this is testtest content";

        postRepository.save(Post.builder().member(member).title(title).build());
        Post testPost = postRepository.save(Post.builder().member(member).content(content).build());
        replyRepository.save(Reply.of(member, post.getId(), content));

        final String keyword = "test";

        // when
        SearchResponse response = postRepository.integrationSearch(keyword);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getResultCount()).isEqualTo(2);
    }
}
