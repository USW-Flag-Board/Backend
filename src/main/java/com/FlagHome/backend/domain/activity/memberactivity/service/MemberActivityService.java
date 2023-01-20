package com.FlagHome.backend.domain.activity.memberactivity.service;

import com.FlagHome.backend.domain.activity.memberactivity.repository.MemberActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberActivityService {
    private final MemberActivityRepository memberActivityRepository;
}
