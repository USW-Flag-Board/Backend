package com.FlagHome.backend.domain.member.sleeping;

import com.FlagHome.backend.domain.member.sleeping.repository.SleepingRepository;
import com.FlagHome.backend.domain.member.sleeping.service.SleepingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class SleepingServiceTest {
    @Autowired
    private SleepingService sleepingService;

    @Autowired
    private SleepingRepository sleepingRepository;
}
