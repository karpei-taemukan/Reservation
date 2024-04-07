package com.zerobase.reservation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class KioskForm {
    @Schema(description = "고객 이름", example = "홍길동")
    private String customerName;
    @Schema(description = "고객 이메일", example = "AAA123@naver.com")
    private String customerEmail;
}
