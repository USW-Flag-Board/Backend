package com.FlagHome.backend.domain.member.entity.enums;

import com.FlagHome.backend.global.common.CustomEnumDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;

@JsonDeserialize(using = CustomEnumDeserializer.class)
public enum Major {
    // 인문사회대학
    국어국문, 사학, 영어영문학, 프랑스어문학, 러시아어문학, 일어일문, 중어중문, 법, 행정, 미디어커뮤니케이션,

    // 경상대학
    경영, 글로벌비지니스, 회계, 경제금융, 국제개발협력, 호텔경영, 외식경영, 관광경영,

    // 공과대학
    바이오사이언스, 바이오공학마케팅, 융합화학산업, 건설환경공학, 환경에너지공학, 건축, 도시부동산,
    산업공학, 기계공학, 전자재료공학, 전자물리, 전기공학, 전자공학, 신소재공학, 화학공학,

    // ICT대학
    컴퓨터SW, 미디어SW, 정보통신, 정보보호, 데이터과학, 클라우드융복합,

    // 건강과학대학
    간호, 아동가족복지, 의류, 식품영양, 스포츠과학,

    // 미술대학
    회화, 조소, 커뮤니케이션디자인, 패션디자인, 공예디자인,

    // 음악대학
    작곡, 성악, 피아노, 관현악, 국악,

    // 융합문화예술대학
    영화영상, 연극, 무용, 문화컨텐츠테크놀로지,

    // 국제대학
    자유전공
}
