package com.Flaground.backend.module.post.domain.enums;

import com.Flaground.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.function.Function;

import static com.Flaground.backend.module.post.domain.QPost.post;

@RequiredArgsConstructor
@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum SearchPeriod {
    ALL((time) -> null),
    DAY(post.createdAt::after),
    WEEK(post.createdAt::after),
    MONTH(post.createdAt::after),
    HALF_YEAR(post.createdAt::after),
    YEAR(post.createdAt::after);

    private final Function<LocalDateTime, BooleanExpression> periodExpression;

    public BooleanExpression toExpression() {
        LocalDateTime time = mapTo();
        return periodExpression.apply(time);
    }

    private LocalDateTime mapTo() {
        return switch (this) {
            case DAY -> LocalDateTime.now().minusDays(1);
            case WEEK -> LocalDateTime.now().minusWeeks(1);
            case MONTH -> LocalDateTime.now().minusMonths(1);
            case HALF_YEAR -> LocalDateTime.now().minusMonths(6);
            case YEAR -> LocalDateTime.now().minusYears(1);
            default -> null;
        };
    }
}
