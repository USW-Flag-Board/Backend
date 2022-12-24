package com.FlagHome.backend.domain.tag.repository;

import com.FlagHome.backend.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
