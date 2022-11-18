package com.FlagHome.backend.v1.tag.repository;

import com.FlagHome.backend.v1.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {
}
