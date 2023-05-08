package com.Flaground.backend.module.auth;

import com.Flaground.backend.common.IntegrationTest;
import com.Flaground.backend.module.auth.domain.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

public class AuthControllerTest extends IntegrationTest {
    private static final String BASE_URI = "/auth";
    @Autowired
    private AuthRepository authRepository;

//    @Test
//    public void 인증시간_만료로_회원가입_실패() throws Exception {
//        // given
//        final String email = "gmlwh124@suwon.ac.kr";
//        final String certification = RandomGenerator.getRandomNumber();
//        final LocalDateTime expireAt = LocalDateTime.now().minusMinutes(10);
//
//        AuthInformation authInformation = AuthInformation.builder()
//                .email(email)
//                .joinType(JoinType.NORMAL)
//                .certification(certification)
//                .build();
//
//        authInformation.setCreatedAt(expireAt);
//        authRepository.save(authInformation);
//
//        SignUpRequest request = SignUpRequest.builder().email(email).certification(certification).build();
//        String uri = BASE_URI + "/sign-up";
//
//        // when
//        ResultActions resultActions = mockMvc.perform(post(uri)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(request)));
//
//        // then
//        resultActions
//                .andExpect(status().isBadRequest())
//                .andExpect(jsonPath("errorCode").value(ErrorCode.EXPIRED_AUTHENTICATION_TIME.toString()))
//                .andExpect(jsonPath("message").value(ErrorCode.EXPIRED_AUTHENTICATION_TIME.getMessage()))
//                .andDo(print());
//    }
}
