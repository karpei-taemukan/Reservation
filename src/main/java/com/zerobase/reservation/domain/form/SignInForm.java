package com.zerobase.reservation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import lombok.Getter;

@Getter
public class SignInForm {

    @Column(unique = true)
    @Schema(description = "(고객 혹은 점장) 이름", example = "홍길동")
    private String name;

    @Schema(description = "(고객 혹은 점장) 이메일", example = "AAA123@naver.com  // (무료계정이라 메일건(mailgun)에 가입된 이메일이여야합니다)")
    private String email;

    @Schema(description = "(고객 혹은 점장) 비밀번호", example = "AAAA111222")
    private String password;
}
