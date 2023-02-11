package com.cmc12th.runway.data.model

enum class CommonReponseStatus(private val errorCode: String, private val message: String) {

    /**
     * 잘못된 요청
     */
    _INTERNAL_SERVER_ERROR("COMMON000", "서버 에러, 관리자에게 문의 바랍니다."),
    _BAD_REQUEST("COMMON001", "잘못된 요청입니다."),
    _UNAUTHORIZED("COMMON002", "권한이 잘못되었습니다"),
    _METHOD_NOT_ALLOWED("COMMON003", "지원하지 않는 Http Method 입니다."),

    /**
     * 인증 관련 에러코드
     */
    ForbiddenException("AUTH002", "해당 요청에 대한 권한이 없습니다."),
    UNAUTHORIZED_EXCEPTION("AUTH003", "로그인 후 이용가능합니다. 토큰을 입력해 주세요"),
    EXPIRED_JWT_EXCEPTION("AUTH004", "기존 토큰이 만료되었습니다. 토큰을 재발급해주세요."),
    RELOGIN_EXCEPTION("AUTH005", "모든 토큰이 만료되었습니다. 다시 로그인해주세요."),
    INVALID_TOKEN_EXCEPTION("AUTH006", "토큰이 올바르지 않습니다."),
    HIJACK_JWT_TOKEN_EXCEPTION("AUTH007", "탈취된(로그아웃 된) 토큰입니다 다시 로그인해주세요."),
    INVALID_REFRESH_TOKEN("AUTH009", "리프레쉬 토큰이 유효하지 않습니다."),


    /**
     * 1000 : 소셜 관련 예외 처리
     */
    APPLE_BAD_REQUEST("OAUTH001", "애플 토큰이 잘못되었습니다."),
    APPLE_SERVER_ERROR("OAUTH002", "애플 서버와 통신에 실패 하였습니다."),
    FAIL_TO_MAKE_APPLE_PUBLIC_KEY("OAUTH003", "새로운 애플 공개키 생성에 실패하였습니다."),

    /**
     * UXXX : USER 관련 에러
     */
    USERS_EMPTY_USER_ID("U001", "유저 아이디 값을 입력해주세요."),
    USERS_EMPTY_USER_PASSWORD("U002", "유저 비밀번호를 입력해주세요."),
    TOO_SHORT_PASSWORD("U003", "비밀번호의 길이를 8자 이상을 설정해주세요."),
    FAILED_TO_SIGN_UP("U004", "회원가입에 실패하였습니다."),
    USERS_EXISTS_ID("U005", "중복된 전화번호입니다."),
    USERS_EXISTS_NICKNAME("U006", "중복된 닉네임입니다."),
    POST_USERS_EMPTY_NICKNAME("U007", "닉네임을 입력해주세요."),
    FAILED_TO_LOGIN("U008", "로그인에 실패하였습니다."),
    NOT_CORRECT_PASSWORD("U010", "비밀번호가 일치하지 않습니다."),
    ALREADY_DELETED_USER("U011", "이미 탈퇴된 유저입니다."),
    NOT_CORRECT_USER("U012", "회원 정보에 일치하는 아이디가 없습니다."),
    NOT_EXIST_KAKAO("U013", "회원정보에 존재하는 카카오가 존재하지 않습니다. 회원가입 해주세요"),
    NOT_POST_CONTENT("U015", "내용을 입력해주세요."),
    NOT_CORRECT_PHONE_NUMBER_FORM("U016", "전화번호를 하이픈(-) 없이 입력해주세요"),
    NOT_CORRECT_PASSWORD_FORM("U017", "비밀번호는 영문과 특수문자 숫자를 포함하며 8자 이상이어야 합니다."),
    FAIL_UPLOAD_IMG("U018", "사진 업로드에 실패했습니다."),
    WRONG_FORMAT_FILE("U019", "잘못된 형식에 이미지 파일입니다."),
    CATEGORY_EMPTY_USERS("U020", "취향을 하나 이상 등록해주세요"),
    NOT_EXIST_USER("U021", "존재하지 않는 유저입니다."),
    USER_NOT_FOUND("U022", "가입되지 않은 유저입니다. 추가정보를 입력해 주세요"),
    KAKAO_SERVER_ERROR("U023", "카카오 서버 에러"),
    USERS_EXISTS_SOCIAL_ID("U024", "중복된 소셜 ID 입니다."),
    LIMIT_CERTIFICATE_SMS("U025", "해당 번호는 인증 횟수를 초과 하였습니다. 10분뒤에 시도해주세요"),
    NOT_CORRECT_CONFIRM_NUM("U026", "인증번호가 일치하지 않습니다."),
    NOT_EXIST_CONFIRM_NUM("U027", "요청한 전화번호가 존재하지 않습니다."),
    NOT_EXIST_STORE("S001", "해당 가게는 존재하지 않습니다."),

    /** 에러코드를 못찾을 때 Default값 */
    NO_ERROR_CODE("NOT_EXIST_ERROR", "알 수 없는 에러가 발생했습니다.");

    companion object {
        fun of(errorCode: String) = values().find { it.errorCode == errorCode } ?: NO_ERROR_CODE
    }

}