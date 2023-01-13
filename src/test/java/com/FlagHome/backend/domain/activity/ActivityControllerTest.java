package com.FlagHome.backend.domain.activity;


import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Transactional
@SpringBootTest
public class ActivityControllerTest {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    private Member dummyMember1;



    @BeforeEach
    public void testSetting() {
        dummyMember1 = memberRepository.save(Member.builder()
                .loginId("gogun1")
                .password("1234")
                //.email("yabueng@suwon.ac.kr")
                .studentId("1").build());

/*
        @Nested
        @DisplayName("활동 삭제 테스트")
        class deleteActivityTest {
            @Test
            @DisplayName("활동 삭제 성공")
            void deleteActivitySuccessTest() {
                //given
                Activity deletedactivity = activityRepository.save(Activity.builder()
                                .name("삭제될 활동 제목")
                                .description("삭제될 활동 내용")
                                .leader(dummyMember1)
                                .build());

                Long deleteActivityId = deletedactivity.getId();

                //when
                activityService.deleteActivity(deleteActivityId);
                entityManager.clear();

                //then : 정상적으로 삭제되어 찾을수 없는지
                deletedactivity = activityRepository.findById(deleteActivityId).orElse(null);
                assert deletedactivity == null;


            }

            @Test
            @DisplayName("해당 활동의 리더가 아닌 사람이 활동 삭제시 실패")
            void deleteActivityFailTest() {
                Activity activity = activityRepository.save(Activity.builder()
                        .name("삭제될 활동 제목")
                        .description("삭제될 활동 내용")
                        .leader(dummyMember1)
                        .build());

                long deleteActivityId = activity.getId()


                assertThat(member.getId()).isEqualTo(dummyMember1.getId());

            }*/
    }
}