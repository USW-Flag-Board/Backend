package com.FlagHome.backend.v1.member.mail;

public enum MailType {
    AUTH_EMAIL("[FLAG] 재학생 이메일 인증 요청입니다.",
            "<div>" +
                    "재학생 인증이 완료되면 아이디/비밀번호 찾기 기능을 사용하실 수 있습니다.<br>" +
                    "다음 6자리 인증번호를 입력해주세요.</div>"),

    FIND_ID("[FLAG] 아이디 찾기 결과입니다.",
            "<div>아이디 찾기 결과입니다.</div>"),

    REISSUE_PASSWORD("[FLAG] 임시 비밀번호가 발급되었습니다.",
            "<div>로그인을 위한 임시비밀번호가 발급되었습니다.<br>" +
                    "로그인 후 반드시 비밀번호를 변경해주시기 바랍니다.<br>" +
                    "임시 비밀번호는 다음과 같습니다.</div>");

    MailType(String subject, String content) {
        this.subject = subject;
        this.content = content;
    }

    private final String subject;
    private final String content;

    public String getSubject() { return this.subject; }

    public String createMailForm(String result) {
        return "<div>안녕하세요. 수원대 최고의 개발동아리 FLAG입니다.</div>" +
                this.content +
                "<div>" + result + "</div>" +
                "<div>추가적인 문의사항이 있으시다면, gmlwh124@naver.com 으로 문의 바랍니다.<br>" +
                "감사합니다.</div>";
    }
}
