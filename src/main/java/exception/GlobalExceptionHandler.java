package exception;

import jakarta.servlet.ServletException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler({ReservationException.class})
    public ResponseEntity<ExceptionResponse> handleReservationException(
            ReservationException e
    ){
        log.error("{} is occurred:", e.getErrorCode());
        return ResponseEntity.badRequest().body(new ExceptionResponse(e.getMessage(), e.getErrorCode()));
    }


    @ExceptionHandler({ServletException.class})
    public ResponseEntity<String> securityException(final ReservationException e){
        log.warn("api Exception: "+e.getErrorCode());
        return ResponseEntity.badRequest().body("잘못된 인증 시도 입니다");
    }


    @Getter
    @ToString
    @AllArgsConstructor
    public static class ExceptionResponse {
        private String message;
        private ErrorCode errorCode;
    }
}
