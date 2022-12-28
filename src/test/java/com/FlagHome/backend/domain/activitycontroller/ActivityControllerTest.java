package com.FlagHome.backend.domain.activitycontroller;


import com.FlagHome.backend.domain.activity.Type;
import com.FlagHome.backend.domain.activity.entity.Activity;
import com.FlagHome.backend.domain.activity.repository.ActivityRepository;
import com.FlagHome.backend.domain.activity.service.ActivityService;
import com.FlagHome.backend.domain.member.entity.Member;
import com.FlagHome.backend.domain.member.repository.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Transactional
@SpringBootTest
public class ActivityControllerTest {
    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private ActivityService activityService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private EntityManager entityManager;

    private Member dummyMember1;

    private Type dummyType;


    @BeforeEach
    public void testSetting() {
        dummyMember1 = memberRepository.save(Member.builder()
                .loginId("gogun1")
                .password("1234")
                //.email("yabueng@suwon.ac.kr")
                .studentId("1").build());

        dummyType = Type.valueOf("Study");


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
                                .members((List<Member>) dummyMember1)
                                .type(dummyType)
                                .build());

                long deleteActivityId = deletedactivity.getId();

                //when
                activityService.deleteActivity(deleteActivityId);
                entityManager.clear();

                //then : 정상적으로 삭제되어 찾을수 없는지
                deletedactivity = activityRepository.findById(deleteActivityId).orElse(null);
                assert deletedactivity == null;


            }

            /*@Test
            @DisplayName("권한 부족으로 인한 활동 삭제 실패")
            void deleteActivityFailTest() {
                Activity activity = activityRepository.save(Activity.builder()
                        .name("삭제될 활동 제목")
                        .description("삭제될 활동 내용")
                        .members((List<Member>) dummyMember1)
                        .type(dummyType)
                        .build());

                long deleteActivityId = activity.getId();

                assertThat();

            }*/
        }
    }
}