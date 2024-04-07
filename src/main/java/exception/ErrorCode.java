package exception;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    ALREADY_SIGNUP("이미 존재하는 회원"),
    USER_NOT_FOUND("사용자가 없음"),
    LOGIN_CHECK_FAIL("로그인 인증 실패"),
    WRONG_VERIFICATION("잘못된 인증시도"),
    ALREADY_VERIFY("이미 인증됨"),
    EXPIRE_CODE("유효기간이 지난 토큰"),
    NOT_FOUND_STORE("등록 안된 매장"),
    ALREADY_RESERVE("이미 예약함"),
    ALREADY_RESERVED_STORE("예약된 매장"),
    ALREADY_REGISTERED_STORE("이미 등록된 매장"),
    NOT_SAME_CUSTOMER_NAME("고객의 이름이 일치하지 않음"),
    NOT_RESERVED("예약되지 않은 고객"),
    NOT_FOUND_REVIEW("리뷰가 존재하지 않음");

    private final String description;
}
