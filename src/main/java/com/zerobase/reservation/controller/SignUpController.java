package com.zerobase.reservation.controller;

import com.zerobase.reservation.application.SignUpApplication;
import com.zerobase.reservation.domain.form.SignUpForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/signUp")
@RequiredArgsConstructor
@Tag(name = "SignUp-Controller", description = "고객과 점장의 회원가입")
public class SignUpController {

    private final SignUpApplication signUpApplication;

    // **************************************************************
    // 이메일 인증(mailgun 에 가입된 계정이여야 이메일 발송 가능)
    // --> 이유는 무료로 사용하기때문에 등록된 계정만 이메일 발송 가능
    // **************************************************************


    // **************************************************************
    // 주의: 메일건의 도메인과 API 키들을 깃허브에 올리면 따로 문의하지않는 이상
    // 메일건을 이용이 불가함
    // ***************************************************************


    // ***************************************************************
    /*
    * mailgun 에 가입이 완료되면
    * application.yml 파일에 다음과 같이 작성

    * (예시)
    * spring:
         mailgun:
            domain: 메일건 도메인을 작성 (sandbox~ 시작하는 문자열을 작성)
            api: 메일 건 API KEY 을 작성 (Mailgun API keys -> Add new key 에서 발급)

    * */
    // ***************************************************************




    /*
    * 고객(customer)의 회원 가입과 이메일 인증
    * */

    @PostMapping("/customer")
    @Operation(summary = "고객의 회원가입")
    public ResponseEntity<String> signUpCustomer(
            @RequestBody  SignUpForm form
    ){

        return ResponseEntity.ok(signUpApplication.customerSignUp(form));
    }


    /// 메일이 오면

    // Hello ZeroBase! Please Click Link for verification.
    //http://localhost:8083/signUp/verify/customer?email=~~~@gmail.com&code=ABCD1234

    // 위와 같은 메일에 대한 GetMapping

    @GetMapping("/verify/customer")
    @Operation(summary = "이메일로 인증메일 왔을 경우 링크를 클릭해 인증", description = "인증메일의 링크에는 랜덤 코드가 있기 때문에 이메일로 확인해야 합니다")
    public ResponseEntity<String> verifyCustomer(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "code") String code
    ){
        signUpApplication.customerVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }


    // ----------------------------------------------------------------------------------------



    /*
     * 점장(seller)의 회원 가입과 이메일 인증
     * */

    @PostMapping("/seller")
    @Operation(summary = "점장의 회원가입")
    public ResponseEntity<String> signUpSeller(
            @RequestBody SignUpForm form
    ){
        return ResponseEntity.ok(signUpApplication.sellerSignUp(form));
    }

    // 이메일 인증


    /// 메일이 오면

    // Hello ZeroBase! Please Click Link for verification.
    //http://localhost:8083/signUp/verify/seller?email=~~~@naver.com&code=ABCD1234

    // 위와 같은 메일에 대한 GetMapping

    @GetMapping("/verify/seller")
    @Operation(summary = "이메일로 인증메일 왔을 경우 링크를 클릭해 인증", description = "인증메일의 링크에는 랜덤 코드가 있기 때문에 이메일로 확인해야 합니다")
    public ResponseEntity<String> verifySeller(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "code") String code
    ){
        signUpApplication.sellerVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }


    // ----------------------------------------------------------------------------------------

    /*
     * 관리자(admin)의 회원 가입과 이메일 인증
     * */
    @PostMapping("/admin")
    @Operation(summary = "관리자의 회원가입")
    public ResponseEntity<String> signUpAdmin(
            @RequestBody SignUpForm form
    ){
        return ResponseEntity.ok(signUpApplication.adminSignUp(form));
    }


    // 이메일 인증
    /// 메일이 오면

    // Hello ZeroBase! Please Click Link for verification.
    //http://localhost:8083/signUp/verify/admin?email=~~~@naver.com&code=ABCD1234

    // 위와 같은 메일에 대한 GetMapping
    @GetMapping("/verify/admin")
    @Operation(summary = "이메일로 인증메일 왔을 경우 링크를 클릭해 인증", description = "인증메일의 링크에는 랜덤 코드가 있기 때문에 이메일로 확인해야 합니다")
    public ResponseEntity<String> verifyAdmin(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "code") String code
    ){
        signUpApplication.adminVerify(email, code);
        return ResponseEntity.ok("인증 완료");
    }


}
