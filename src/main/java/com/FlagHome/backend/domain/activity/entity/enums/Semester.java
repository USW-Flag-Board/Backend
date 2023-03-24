package com.FlagHome.backend.domain.activity.entity.enums;

import com.FlagHome.backend.global.exception.CustomException;
import com.FlagHome.backend.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum Semester {
    SPRING_SEMESTER("1학기", new int[]{3, 4, 5}),
    SUMMER_VACATION("여름방학", new int[]{6, 7, 8}),
    FALL_SEMESTER("2학기", new int[]{9, 10, 11}),
    WINTER_VACATION("겨울방학", new int[]{12, 1, 2});

    private final String semester;
    private final int[] semesterMonth;

    public static Semester findSemester(int month) {
        return Arrays.stream(Semester.values())
                .filter(semester -> hasSemesterMonth(semester, month))
                .findAny()
                .orElseThrow(() -> new CustomException(ErrorCode.INVALID_MONTH_RANGE));
    }

    private static boolean hasSemesterMonth(Semester semester, int targetMonth) {
        return Arrays.stream(semester.semesterMonth)
                .anyMatch(month -> month == targetMonth);
    }
}