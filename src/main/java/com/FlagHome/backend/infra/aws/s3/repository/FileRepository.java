package com.FlagHome.backend.infra.aws.s3.repository;

import com.FlagHome.backend.infra.aws.s3.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
