package com.FlagHome.backend.domain.activity.entity.enums;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Semester {
    SPRING_SEMESTER("1학기", new int[]{3, 4, 5}),
    SUMMER_VACATION("여름방학", new int[]{6, 7, 8}),
    FALL_SEMESTER("2학기", new int[]{9, 10, 11}),
    WINTER_VACATION("겨울방학", new int[]{12, 1, 2});

    private final String semester;
    private final int[] semesterMonth;

    public static Semester findSemester(int month) {
        return Arrays.stream(Semester.values())
                .filter(semester -> containsMonth(semester, month))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MONTH_RANGE));
    }

    private static boolean containsMonth(Semester semester, int targetMonth) {
        return Arrays.stream(semester.semesterMonth)
                .anyMatch(month -> month == targetMonth);
    }


    @Override
    public String toString() {
        return getSemester();
    }
}
