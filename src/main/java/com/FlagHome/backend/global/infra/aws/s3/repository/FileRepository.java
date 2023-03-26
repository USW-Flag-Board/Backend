package com.FlagHome.backend.global.infra.aws.s3.repository;

import com.FlagHome.backend.global.infra.aws.s3.File;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<File, Long> {
}
