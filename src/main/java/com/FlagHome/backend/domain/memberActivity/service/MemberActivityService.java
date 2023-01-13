package com.FlagHome.backend.domain.memberActivity.service;

import com.FlagHome.backend.domain.memberActivity.repository.MemberActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberActivityService {
    private final MemberActivityRepository memberActivityRepository;
}
