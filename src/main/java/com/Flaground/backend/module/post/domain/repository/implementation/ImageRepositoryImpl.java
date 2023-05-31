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

    @Override
    public void deleteImagesByKeys(List<String> keys) { // jpa 사용시 select query 이후 개별로 delete query 발생
        queryFactory
                .delete(image)
                .where(image.key.in(keys))
                .execute();
    }
}
