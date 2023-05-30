package com.Flaground.backend.module.post.domain.repository.implementation;

import com.Flaground.backend.module.post.domain.repository.ImageRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.Flaground.backend.module.post.domain.QImage.image;

@RequiredArgsConstructor
public class ImageRepositoryImpl implements ImageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<String> getKeysByPostId(Long postId) {
        return queryFactory
                .select(image.key)
                .from(image)
                .where(image.postId.eq(postId))
                .fetch();
    }
}
