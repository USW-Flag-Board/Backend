package com.Flaground.backend.module.post.domain.repository;

import com.Flaground.backend.module.post.domain.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long>, ImageRepositoryCustom {
}
