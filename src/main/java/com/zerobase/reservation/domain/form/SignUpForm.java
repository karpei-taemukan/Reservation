package com.zerobase.reservation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SignUpForm {
   @Schema(description = "(고객 혹은 점장 혹은 관리자) 이메일", example = "AAA123@naver.com // (무료계정이라 메일건(mailgun)에 가입된 이메일이여야합니다)")
   private String email;
   @Schema(description = "(고객 혹은 점장 혹은 관리자) 비밀번호", example = "AAAA111222")
   private String password;
   @Schema(description = "(고객 혹은 점장 혹은 관리자) 이름", example = "홍길동")
   private String name;
   @Schema(description = "(고객 혹은 점장 혹은 관리자) 핸드폰 번호", example = "01011112222")
   private String phone;
   @Schema(description = "(고객 혹은 점장 혹은 관리자) 권한",example = "ROLE_CUSTOMER(고객일 경우) or ROLE_SELLER(점장일 경우)")
   private String roles;
}
