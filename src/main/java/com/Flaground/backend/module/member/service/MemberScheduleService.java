package com.Flaground.backend.module.member.service;

import com.Flaground.backend.infra.aws.ses.service.MailService;
import com.Flaground.backend.module.member.domain.Member;
import com.Flaground.backend.module.member.domain.Sleeping;
import com.Flaground.backend.module.member.domain.repository.MemberRepository;
import com.Flaground.backend.module.member.domain.repository.SleepingRepository;
import lombok.RequiredArgsConstructor;
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
    private final MailService mailService;

    //@Scheduled(cron = "000000")
    public void selectingDeactivateMembers() {
        List<Member> deactivateMembers = memberRepository.getDeactivateMembers();
        List<Sleeping> sleepings = convertToSleepings(deactivateMembers);
        sleepingRepository.saveAll(sleepings);
        deactivateMembers(deactivateMembers);
    }

    //@Scheduled(cron = "000000")
    public void sendNotificationToDeactivableMembers() {
        List<String> emailLists = memberRepository.getDeactivableMemberEmails();
        emailLists.forEach(mailService::sendChangeSleep);
    }

    //@Scheduled(cron = "000000")
    public void deleteExpiredSleep() {
        List<Sleeping> sleepingList = sleepingRepository.getAllSleeping();
        sleepingRepository.deleteAllInBatch(sleepingList);
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
