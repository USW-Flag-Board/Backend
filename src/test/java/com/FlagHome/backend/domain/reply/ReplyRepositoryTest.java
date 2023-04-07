package com.FlagHome.backend.domain.reply;

import com.FlagHome.backend.RepositoryTest;
import com.FlagHome.backend.domain.member.entity.Avatar;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.post.entity.Post;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.reply.controller.dto.ReplyResponse;
import com.FlagHome.backend.domain.reply.entity.Reply;
import com.FlagHome.backend.domain.reply.repository.ReplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ReplyRepositoryTest extends RepositoryTest {
    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Member member;

    private Post post;

    @BeforeEach
    public void testSetup() {
        final String nickname = "john";
        Avatar avatar = Avatar.builder().nickname(nickname).build();
        member = memberRepository.save(Member.builder().avatar(avatar).build());
        post = postRepository.save(Post.builder().build());
    }

    @Test
    public void 댓글_가져오기_테스트() {
        // given
        final String content = "content";

        Reply reply1 = Reply.of(member, post, content);
        Reply reply2 = Reply.of(member, post, content);
        Reply reply3 = Reply.of(member, post, content);

        replyRepository.saveAll(List.of(reply1, reply2, reply3));

        // when
        List<ReplyResponse> responses = replyRepository.getAllReplies(post.getId());

        // then
        assertThat(responses.size()).isEqualTo(3);
        assertThat(responses.get(0).getContent()).isEqualTo(content);
    }
}
