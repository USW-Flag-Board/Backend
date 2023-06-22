package com.Flaground.backend.module.member.service;

import com.Flaground.backend.infra.aws.ses.service.AwsSESService;
import com.Flaground.backend.module.activity.service.ActivityService;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.Sleeping;
import com.Flaground.backend.module.member.domain.repository.BlackListRepository;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.member.domain.repository.SleepingRepository;
import com.Flaground.backend.module.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberScheduleService {
    private final MemberRepository memberRepository;
    private final SleepingRepository sleepingRepository;
    private final BlackListRepository blackListRepository;
    private final AwsSESService awsSESService;
    private final ActivityService activityService;
    private final ReportService reportService;

    //@Scheduled(cron = "000000")
    public void selectingDeactivateMembers() {
        List<Member> deactivateMembers = memberRepository.getDeactivateMembers();
        List<Sleeping> sleepings = convertToSleepings(deactivateMembers);
        sleepingRepository.saveAll(sleepings);
        deactivateMembers(deactivateMembers);
    }

//    @Scheduled(cron = "000000")
    public void sendNotificationToDeactivableMembers() {
        List<String> emailLists = memberRepository.getDeactivableMemberEmails();
        emailLists.forEach(awsSESService::sendChangeSleep);
    }

    //@Scheduled(cron = "000000")
    public void deleteExpiredSleep() {
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
    }

//    @Scheduled(cron = "000000")
    public void releaseBannedMembers() {
        blackListRepository.releaseBannedMembers();
    }

    @Scheduled(cron = "0 55 23 * * *")
    public void clearWithdrawMembers() {
        List<Long> withdrawMembers = memberRepository.getWithdrawMembers();
        withdrawMembers.forEach(this::clearMemberData);
    }

    private void clearMemberData(Long memberId) {
        reportService.cleanReports(memberId);
        activityService.cleanActivities(memberId);
    }

    private List<Sleeping> convertToSleepings(List<Member> deactivateMembers) {
        return deactivateMembers.stream()
                .map(Sleeping::of)
                .collect(Collectors.toList());
    }

    private void deactivateMembers(List<Member> memberList) {
        memberList.forEach(Member::deactivate);
    }
}
