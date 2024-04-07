package com.zerobase.reservation.application;


import com.zerobase.reservation.domain.Admin;
import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.domain.Seller;
import com.zerobase.reservation.domain.form.SignInForm;
import com.zerobase.reservation.service.AdminService;
import com.zerobase.reservation.service.CustomerService;
import com.zerobase.reservation.service.SellerService;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserType;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInApplication {
    private final CustomerService customerService;
    private final SellerService sellerService;
    private final AdminService adminService;
    private final JwtAuthProvider provider;


    // 고객이 로그인하면 로그인 토큰 발행
    public String customerLoginToken(SignInForm signInForm){

    /*    System.out.println(signInForm.getName());
        System.out.println(signInForm.getPassword());
        System.out.println(signInForm.getEmail());*/

        // 1. 로그인 가능 여부
        Customer validCustomer
                = customerService.findValidCustomer(signInForm.getEmail(), signInForm.getPassword())
                .orElseThrow(() -> new ReservationException(ErrorCode.LOGIN_CHECK_FAIL));


        // 2. CUSTOMER 타입의 토큰 발행 후 토큰을 response
        return provider.createToken(signInForm.getName(), validCustomer.getId(), UserType.CUSTOMER);
    }


    // 점장이 로그인하면 로그인 토큰 발행
    public String sellerLoginToken(SignInForm signInForm) {
        // 1. 로그인 가능 여부
        Seller validSeller =
                sellerService.findValidSeller(signInForm.getEmail(), signInForm.getPassword())
                        .orElseThrow(()->new ReservationException(ErrorCode.LOGIN_CHECK_FAIL));

        System.out.println("SELLER PW  "+ validSeller.getPassword());

        // 2. SELLER 타입의 토큰 발행 후 토큰을 response
        return provider.createToken(signInForm.getName(), validSeller.getId(), UserType.SELLER);
    }


    public String adminLoginToken(SignInForm signInForm) {
        // 1. 로그인 가능 여부
        Admin validAdmin =
                adminService.findValidAdmin(signInForm.getEmail(), signInForm.getPassword())
                        .orElseThrow(()->new ReservationException(ErrorCode.LOGIN_CHECK_FAIL));

        // 2. SELLER 타입의 토큰 발행 후 토큰을 response
        return provider.createToken(signInForm.getName(), validAdmin.getId(), UserType.ADMIN);
    }
}