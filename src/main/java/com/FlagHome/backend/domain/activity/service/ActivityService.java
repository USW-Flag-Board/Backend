package com.FlagHome.backend.domain.activity.service;

import org.springframework.stereotype.Service;

@Service
public interface ActivityService {
    long create();
    void get();
    void update();
    void delete();
}
