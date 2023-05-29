package com.Flaground.backend.infra.aws.s3.domain.repository;

import com.Flaground.backend.infra.aws.s3.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
