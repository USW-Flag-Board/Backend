package com.FlagHome.backend.domain.member;

import com.FlagHome.backend.domain.activity.memberactivity.dto.ParticipateResponse;
import com.FlagHome.backend.domain.activity.memberactivity.service.MemberActivityService;
import com.FlagHome.backend.domain.board.enums.SearchType;
import com.FlagHome.backend.domain.mail.service.MailService;
import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.entity.Avatar;
import com.FlagHome.backend.domain.member.avatar.service.AvatarService;
import com.FlagHome.backend.domain.member.dto.FindResponse;
import com.FlagHome.backend.domain.member.dto.MemberProfileResponse;
import com.FlagHome.backend.domain.member.dto.SearchMemberResponse;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import com.FlagHome.backend.domain.member.service.MemberService;
import com.FlagHome.backend.domain.post.dto.PostDto;
import com.FlagHome.backend.domain.post.repository.PostRepository;
import com.FlagHome.backend.domain.token.entity.FindRequestToken;
import com.FlagHome.backend.domain.token.entity.Token;
import com.FlagHome.backend.domain.token.service.FindRequestTokenService;
import com.FlagHome.backend.global.utility.InputValidator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class MemberServiceSliceTest {
    @InjectMocks
    private MemberService memberService;

    @Mock
    private MailService mailService;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private InputValidator inputValidator;

    @Mock
    private FindRequestTokenService findRequestTokenService;

    @Mock
    private MemberActivityService memberActivityService;

    @Mock
    private AvatarService avatarService;

    @Mock
    private PostRepository postRepository;

    @Test
    @DisplayName("아이디 찾기 테스트")
    void findIdTest() {
        // given
        String name = "문희조";
        String email = "gmlwh124@suwon.ac.kr";
        String certification = "123456";
        LocalDateTime expireAt = LocalDateTime.now();
        
        Member member = Member.builder()
                .name(name)
                .email(email)
                .build();
        
        Token findRequestToken = FindRequestToken.builder()
                .key(email)
                .value(certification)
                .expiredAt(expireAt)
                .build();

        doNothing().when(inputValidator).validateUSWEmail(anyString());
        given(memberRepository.findByEmail(email)).willReturn(Optional.of(member));
        given(findRequestTokenService.issueToken(anyString(), anyString())).willReturn(findRequestToken);
        doNothing().when(mailService).sendFindCertification(anyString(), anyString());

        // when
        FindResponse findResponse = memberService.findId(name, email);

        // then
        then(inputValidator).should(times(1)).validateUSWEmail(anyString());
        then(findRequestTokenService).should(times(1)).issueToken(anyString(), anyString());
        then(mailService).should(times(1)).sendFindCertification(anyString(), anyString());
        assertThat(findResponse.getEmail()).isEqualTo(email);
        assertThat(findResponse.getDeadLine()).isEqualTo(expireAt);
    }

    @Test
    @DisplayName("비밀번호 찾기 테스트")
    void findPasswordTest() {
        // given
        String loginId = "gmlwh124";
        String email = "gmlwh124@suwon.ac.kr";
        String certification = "123456";
        LocalDateTime expireAt = LocalDateTime.now();

        Member member = Member.builder()
                .loginId(loginId)
                .email(email)
                .build();

        Token findRequestToken = FindRequestToken.builder()
                .key(email)
                .value(certification)
                .expiredAt(expireAt)
                .build();

        doNothing().when(inputValidator).validateUSWEmail(anyString());
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));
        given(findRequestTokenService.issueToken(anyString(), anyString())).willReturn(findRequestToken);
        doNothing().when(mailService).sendFindCertification(anyString(), anyString());

        // when
        FindResponse findResponse = memberService.findPassword(loginId, email);

        // then
        then(inputValidator).should(times(1)).validateUSWEmail(anyString());
        then(memberRepository).should(times(1)).findByEmail(anyString());
        then(findRequestTokenService).should(times(1)).issueToken(anyString(), anyString());
        then(mailService).should(times(1)).sendFindCertification(anyString(), anyString());
        assertThat(findResponse.getEmail()).isEqualTo(email);
        assertThat(findResponse.getDeadLine()).isEqualTo(expireAt);
    }

    @Test
    @DisplayName("인증번호 인증 테스트")
    void validateCertificationTest() {
        // given
        String email = "gmlwh124@suwon.ac.kr";
        String certification = "123456";
        LocalDateTime expireAt = LocalDateTime.now().plusMinutes(5);

        Member member = Member.builder().build();

        Token findRequestToken = FindRequestToken.builder()
                .key(email)
                .value(certification)
                .expiredAt(expireAt)
                .build();

        given(findRequestTokenService.findToken(anyString())).willReturn(findRequestToken);
        doNothing().when(inputValidator).validateCertification(anyString(), anyString());
        given(memberRepository.findByEmail(anyString())).willReturn(Optional.of(member));

        // when
        memberService.validateCertification(email, certification);

        // then
        Token findToken = findRequestTokenService.findToken(email);
        then(findRequestTokenService).should(times(2)).findToken(anyString());
        then(inputValidator).should(times(1)).validateCertification(anyString(), anyString());
        assertThat(findRequestToken.getKey()).isEqualTo(findToken.getKey());
        assertThat(findRequestToken.getExpiredAt()).isEqualTo(findToken.getExpiredAt());
    }

    @Test
    @DisplayName("멤버 프로필 가져오기 테스트")
    void getMemberProfileTest() {
        // given
        String loginId = "gmlwh124";

        AvatarResponse avatarResponse = AvatarResponse.builder()
                .loginId(loginId)
                .build();

        List<ParticipateResponse> participateResponseList = new ArrayList<>();
        List<PostDto> postDtoList = new ArrayList<>();

        given(avatarService.getAvatar(anyString())).willReturn(avatarResponse);
        given(memberActivityService.getAllActivitiesOfMember(anyString())).willReturn(participateResponseList);
        given(postRepository.findBoardWithCondition(any(), any(SearchType.class), anyString())).willReturn(postDtoList);

        // when
        MemberProfileResponse response = memberService.getMemberProfile(loginId);

        // then
        then(avatarService).should(times(1)).getAvatar(anyString());
        then(memberActivityService).should(times(1)).getAllActivitiesOfMember(anyString());
        then(postRepository).should(times(1)).findBoardWithCondition(any(), any(SearchType.class), anyString());
        assertThat(response.getAvatarResponse().getLoginId()).isEqualTo(loginId);
        assertThat(response.getActivityList().size()).isEqualTo(0);
        assertThat(response.getPostList().size()).isEqualTo(0);
    }

    @Test
    @DisplayName("내 프로필 가져오기 테스트")
    void getMyProfileTest() {
        // given
        Long id = 1L;
        String email = "gmlwh124@suwon.ac.kr";
        String name = "문희조";

        MyProfileResponse myProfileResponse = MyProfileResponse.builder()
                .name(name)
                .email(email)
                .build();

        given(avatarService.getMyProfile(anyLong())).willReturn(myProfileResponse);

        // when
        MyProfileResponse response = memberService.getMyProfile(id);

        // then
        then(avatarService).should(times(1)).getMyProfile(anyLong());
        assertThat(response.getName()).isEqualTo(myProfileResponse.getName());
        assertThat(response.getEmail()).isEqualTo(myProfileResponse.getEmail());
    }

    @Test
    @DisplayName("회원 이름으로 검색 테스트")
    void searchByMemberName() {
        //given
        Member member1 = Member.builder().major(Major.정보보호).id(1L).name("홍길동").build();
        Member member2 = Member.builder().major(Major.컴퓨터SW).id(2L).name("김길동").build();
        List<Member> memberList = List.of(member1,member2);

        String name = "길동";

        given(memberRepository.findByMemberName(Mockito.anyString())).willReturn(memberList);

        //when
        List<SearchMemberResponse> resultList = memberService.searchByMemberName(name);

        //then

        assertThat(resultList.get(0).getName()).contains(name);
        assertThat(resultList.get(1).getName()).contains(name);

    }
}
