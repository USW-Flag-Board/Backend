package com.FlagHome.backend.v1.post.file.repository;

import com.FlagHome.backend.v1.post.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
