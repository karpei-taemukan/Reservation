package com.zerobase.reservation.controller;

import com.zerobase.reservation.application.SignInApplication;
import com.zerobase.reservation.domain.form.SignInForm;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/signIn")
@RequiredArgsConstructor
@Tag(name = "SignIn-Controller", description = "고객과 점장의 SignIn")
public class SignInController {

    private final SignInApplication signInApplication;


    /*
    * 고객의 로그인 함과 동시에
    * JWT 토큰 발행
    * */
    @PostMapping("/customer")
    @Operation(summary = "고객의 로그인과 동시에 JWT 토큰 발행")
    public ResponseEntity<String> signInCustomer(
            @RequestBody SignInForm form
    ){

        return ResponseEntity.ok(signInApplication.customerLoginToken(form));
    }


    /*
     * 점장의 로그인 함과 동시에
     * JWT 토큰 발행
     * */
   @PostMapping("/seller")
   @Operation(summary = "점장의 로그인과 동시에 JWT 토큰 발행")
    public ResponseEntity<String> signInSeller(
            @RequestBody SignInForm form
    ){
        return ResponseEntity.ok(signInApplication.sellerLoginToken(form));
    }


    /*
     * 관리자의 로그인 함과 동시에
     * JWT 토큰 발행
     * */
    @PostMapping("/admin")
    @Operation(summary = "고객의 로그인과 동시에 JWT 토큰 발행")
    public ResponseEntity<String> signInAdmin(
            @RequestBody SignInForm form
    ){
        return ResponseEntity.ok(signInApplication.adminLoginToken(form));
    }
}