package com.FlagHome.backend.domain.member.avatar.repository;

import com.FlagHome.backend.domain.member.avatar.dto.AvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.MyProfileResponse;
import com.FlagHome.backend.domain.member.avatar.dto.QAvatarResponse;
import com.FlagHome.backend.domain.member.avatar.dto.QMyProfileResponse;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.FlagHome.backend.domain.member.QMember.member;
import static com.FlagHome.backend.domain.member.avatar.entity.QAvatar.avatar;
import static com.querydsl.core.types.dsl.Expressions.asString;

@Repository
@RequiredArgsConstructor
public class AvatarRepositoryImpl implements AvatarRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public AvatarResponse getAvatar(String loginId) {
        return queryFactory
                .select(new QAvatarResponse(
                        asString(loginId).as(member.loginId),
                        avatar.nickName,
                        avatar.bio,
                        avatar.profileImage))
                .from(avatar)
                .innerJoin(avatar.member, member)
                .where(member.loginId.eq(loginId))
                .fetchOne();
    }

    @Override
    public MyProfileResponse getMyProfile(long memberId) {
        return queryFactory
                .select(new QMyProfileResponse(
                        avatar.nickName,
                        avatar.bio,
                        avatar.profileImage,
                        member.name,
                        member.email,
                        member.major,
                        member.studentId,
                        member.phoneNumber))
                .from(avatar)
                .innerJoin(avatar.member, member)
                .where(member.id.eq(memberId))
                .fetchOne();
    }

    @Override // jpa를 사용하면 select 쿼리 추가 발생
    public void deleteByMemberId(long memberId) {
        queryFactory
                .delete(avatar)
                .where(avatar.member.id.eq(memberId))
                .execute();
    }
}
