package com.Flaground.backend.module.board;

import com.Flaground.backend.common.IntegrationTest;
import com.Flaground.backend.module.board.domain.Board;
import com.Flaground.backend.module.board.domain.BoardType;
import com.Flaground.backend.module.board.domain.repository.BoardRepository;
import com.Flaground.backend.module.member.domain.Avatar;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.enums.Role;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.Collection;

import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class BoardControllerTest extends IntegrationTest {
    private static final String BASE_URI = "/boards";
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BoardRepository boardRepository;

    private Member member;

    private Board board;

    @BeforeEach
    void setup() {
        final String email = "gmlwh124@suwon.ac.kr";
        final String nickname = "john";
        final String boardName = "자유게시판";
        final BoardType boardType = BoardType.main;
        final Role role = Role.ROLE_ADMIN;

        Avatar avatar = Avatar.builder().nickname(nickname).build();
        member = memberRepository.save(Member.builder()
                .email(email)
                .avatar(avatar)
                .role(role)
                .build());

        board = boardRepository.save(Board.builder().boardType(boardType).name(boardName).build());
        setSecurityContext(member);
    }

    @Test
    public void 게시판_목록_가져오기_테스트() throws Exception {
        // given
        final BoardType boardType = BoardType.main;

        // when
        ResultActions resultActions = mockMvc.perform(get(BASE_URI)
                .param("type", boardType.toString())
                .contentType(MediaType.APPLICATION_JSON));

        // then
        resultActions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.payload.boards[0].boardName", is(board.getName())))
                .andExpect(jsonPath("$.payload.type", is(boardType.toString())))
                .andDo(print());
    }

    private void setSecurityContext(Member member) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> String.valueOf(member.getRole()));

        UserDetails principal = new User(String.valueOf(member.getId()), "", authorities);
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, "", authorities));
    }
}
