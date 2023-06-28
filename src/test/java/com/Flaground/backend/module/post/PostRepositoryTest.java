package com.Flaground.backend.module.post;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.global.common.response.SearchResponse;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepository;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.post.controller.dto.response.GetPostResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostDetailResponse;
import com.Flaground.backend.module.post.controller.dto.response.PostResponse;
import com.Flaground.backend.module.post.controller.dto.response.ReplyResponse;
import com.Flaground.backend.module.post.domain.Post;
import com.Flaground.backend.module.post.domain.PostData;
import com.Flaground.backend.module.post.domain.Reply;
import com.Flaground.backend.module.post.domain.enums.SearchOption;
import com.Flaground.backend.module.post.domain.enums.SearchPeriod;
import com.Flaground.backend.module.post.domain.enums.TopPostCondition;
import com.Flaground.backend.module.post.domain.repository.PostRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

class PostRepositoryTest extends RepositoryTest {
    @Autowired
    private PostRepository postRepository;

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
    @DisplayName("공지사항 5개를 가져와야 한다.")
    void getNoticeTest() {
        // given
        String boardName = "NOTICE";
        List<Post> posts = IntStream.range(0, 6)
                .mapToObj(i -> Post.builder()
                        .title(String.valueOf(i))
                        .boardName(boardName)
                        .build())
                .toList();

        postRepository.saveAll(posts);

        // when
        List<PostResponse> notice = postRepository.getNotice();

        // then
        assertThat(notice).isNotEmpty().hasSize(5);
        assertThat(notice.get(0).getAuthor()).isEqualTo("관리자");
    }

    @Nested
    class 게시글_페이징_테스트 {
        private final int pageSize = 10;
        private final String title = "title";
        private final String boardName = "자유게시판";

        @Test
        void 일반_페이징_테스트() {
            // given
            final int totalSize = 100;
            List<Post> posts = IntStream.rangeClosed(1, totalSize - 1) // beforeEach 때문에
                    .mapToObj(i -> Post.builder().member(member).boardName(boardName).title(title + i).build())
                    .toList();

            postRepository.saveAll(posts);

            Pageable request1 = PageRequest.ofSize(pageSize);
            Pageable request2 = PageRequest.of(totalSize / pageSize, pageSize);

            // when
            Page<PostResponse> responses1 = postRepository.getPostsOfBoard(boardName, request1);
            Page<PostResponse> responses2 = postRepository.getPostsOfBoard(boardName, request2);

            // then
            assertThat(responses1.getTotalPages()).isEqualTo(pageSize);
            assertThat(responses2.getTotalPages()).isEqualTo(pageSize);
        }

        @Test
        void 게시글_없음_페이징_테스트() {
            // given
            postRepository.deleteAll();
            Pageable pageRequest = PageRequest.ofSize(10);

            // when
            Page<PostResponse> responses = postRepository.getPostsOfBoard(boardName, pageRequest);

            // then
            assertThat(responses.getTotalPages()).isZero();
        }
    }

    @Test
    void 게시글_상세보기_테스트() {
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
        assertThat(detailResponse.getLike().isLiked()).isFalse();
        assertThat(detailResponse.getLike().getLikeCount()).isZero();
        assertThat(replyResponses).hasSize(2);
        assertThat(replyResponses.get(0).getContent()).isEqualTo(content);
        assertThat(replyResponses.get(0).getLoginId()).isEqualTo(loginId);
        assertThat(replyResponses.get(0).getNickname()).isEqualTo(nickname);
        assertThat(replyResponses.get(0).getLike().isLiked()).isFalse();
        assertThat(replyResponses.get(0).getLike().getLikeCount()).isZero();
    }

    @Nested
    class 탑_게시글_가져오기_테스트 {
        @Test
        void 핫게시글_가져오기_테스트() {
            // given
            final TopPostCondition condition = TopPostCondition.like;

            postRepository.save(Post.builder().member(member).build());
            post.like();

            // when
            List<PostResponse> responses = postRepository.getTopFiveByCondition(condition);

            // then
            assertThat(responses).hasSize(1);
            assertThat(responses.get(0).getLikeCount()).isEqualTo(1);
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
            assertThat(responses).hasSize(3);
        }
    }

    @Nested
    class 게시글_검색_테스트 {
        private Board board;
        private final String boardName = "자유게시판";
        private final SearchPeriod period = SearchPeriod.ALL;

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
            final SearchOption option = SearchOption.TITLE;

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
            final SearchOption option = SearchOption.CONTENT;

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(2);
        }

        @Test
        void 게시글_댓글_검색_테스트() {
            // given
            final String keyword = "test";
            final String notKeyword = "not";
            final SearchOption option = SearchOption.REPLY;
            PostData data = PostData.builder().boardName(boardName).content(keyword).build();
            Post searchPost = postRepository.save(Post.of(member, data));
            Post notSearchPost = postRepository.save(Post.of(member, data));

            searchPost.addReply(Reply.of(member, searchPost.getId(), keyword));
            searchPost.addReply(Reply.of(member, searchPost.getId(), keyword)); // 중복
            notSearchPost.addReply(Reply.of(member, notSearchPost.getId(), notKeyword));

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then : searchPost는 댓글 때문에 출력되므로 결과는 1개
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(1);
        }

        @Test
        void 게시글_내용_댓글_검색_테스트() {
            // given
            final String keyword = "test";
            final String notKeyword = "imnot";
            final SearchOption option = SearchOption.CONTENT_AND_REPLY;

            Post searchPost = Post.builder().member(member).boardName(boardName).content(notKeyword).build();
            Post notSearchPost = Post.builder().member(member).boardName(boardName).content(keyword).build();
            postRepository.saveAll(List.of(searchPost, notSearchPost));

            searchPost.addReply(Reply.of(member, searchPost.getId(), keyword));
            searchPost.addReply(Reply.of(member, searchPost.getId(), keyword)); // 중복
            notSearchPost.addReply(Reply.of(member, notSearchPost.getId(), notKeyword)); // 중복

            // when
            SearchResponse response = postRepository.searchWithCondition(boardName, keyword, period, option);

            // then : searchPost는 댓글 때문에, notSearchPost는 본인 게시글 때문에 2개가 나와야함
            assertThat(response).isNotNull();
            assertThat(response.getResultCount()).isEqualTo(2);
        }

        @Test
        void 게시글_작성자_검색_테스트() {
            // given
            postRepository.save(Post.builder().member(member).boardName(boardName).build());

            final String keyword = "john";
            final SearchOption option = SearchOption.AUTHOR;

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
        postRepository.save(Post.builder().member(member).content(content).build());

        final String keyword = "test";

        // when
        SearchResponse response = postRepository.integrationSearch(keyword);

        // then
        assertThat(response).isNotNull();
        assertThat(response.getResultCount()).isEqualTo(2);
    }
}
