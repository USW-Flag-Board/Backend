package com.Flaground.backend.module.member;

import com.Flaground.backend.common.RepositoryTest;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MemberRepositoryTest extends RepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Test
    void 로그인_아이디들로_멤버_찾기_테스트() {
        // given
        final String loginId1 = "hejow1";
        final String loginId2 = "hejow2";
        final String loginId3 = "hejow3";

        memberRepository.save(Member.builder().loginId(loginId1).build());
        memberRepository.save(Member.builder().loginId(loginId2).build());
        memberRepository.save(Member.builder().loginId(loginId3).build());

        // when
        List<Member> members = memberRepository.findByLoginIdIn(List.of(loginId1, loginId2, loginId3));

        // then
        assertThat(members.size()).isEqualTo(3);
    }
}
