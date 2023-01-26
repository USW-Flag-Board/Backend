package com.FlagHome.backend.domain.member.avatar.service;

import com.FlagHome.backend.domain.member.avatar.repository.AvatarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AvatarService {
    private final AvatarRepository avatarRepository;

}
