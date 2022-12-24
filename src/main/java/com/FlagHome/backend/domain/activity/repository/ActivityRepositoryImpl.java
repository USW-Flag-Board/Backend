package com.FlagHome.backend.domain.activity.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import javax.transaction.Transactional;

@Repository
@Transactional
@RequiredArgsConstructor
public class ActivityRepositoryImpl implements ActivityRepositoryCustom {
}
