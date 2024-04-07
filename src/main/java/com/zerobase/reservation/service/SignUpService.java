package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Admin;
import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.domain.Seller;
import com.zerobase.reservation.domain.form.SignUpForm;
import com.zerobase.reservation.repository.AdminRepository;
import com.zerobase.reservation.repository.CustomerRepository;
import com.zerobase.reservation.repository.SellerRepository;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SignUpService {
    private final CustomerRepository customerRepository;
    private final SellerRepository sellerRepository;
    private final AdminRepository adminRepository;

    // ###############################################################################

    // 가입된 고객 이메일이 있는 지 확인
    public boolean isCustomerEmailExist(String email){
        return customerRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    // 가입된 점장 이메일이 있는 지 확인
    public boolean isSellerEmailExist(String email){
        return sellerRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    // 가입된 관리자 이메일이 있는 지 확인
    public boolean isAdminEmailExist(String email) {
        return adminRepository.findByEmail(email.toLowerCase(Locale.ROOT)).isPresent();
    }

    // -------------------------------------------------------------------------------------------------


    // 고객의 회원 가입후 CustomerRepository 에 저장
    public Customer signUpCustomer(SignUpForm form) {
        return customerRepository.save(Customer.from(form));
    }


    // 점장의 회원 가입후 SellerRepository 에 저장
    public Seller signUpSeller(SignUpForm form) {
        return sellerRepository.save(Seller.from(form));
    }

    // 관리자의 회원 가입후 AdminRepository 에 저장
    public Admin signUpAdmin(SignUpForm form) {
        return adminRepository.save(Admin.from(form));
    }



    //-------------------------------------------------------------------------------------------------



    // 인증 이메일을 보냄과 동시에 인증 유효기간은 보낸 시점에서 하루로 설정
    @Transactional
    public LocalDateTime ChangeCustomerValidateEmail(Long customerId, String verificationCode){

        Optional<Customer> customerOptional
                = customerRepository.findById(customerId);
        if(customerOptional.isPresent()){
            Customer customer = customerOptional.get();

            customer.setVerificationCode(verificationCode);
            customer.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return customer.getVerifyExpiredAt();
        }
        throw new ReservationException(ErrorCode.USER_NOT_FOUND);
    }


    // 인증 이메일을 보냄과 동시에 인증 유효기간은 보낸 시점에서 하루로 설정
    @Transactional
    public LocalDateTime ChangeSellerValidateEmail(Long sellerId, String verificationCode){

        Optional<Seller> sellerOptional
                = sellerRepository.findById(sellerId);
        if(sellerOptional.isPresent()){
            Seller seller = sellerOptional.get();

            seller.setVerificationCode(verificationCode);
            seller.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return seller.getVerifyExpiredAt();
        }
        throw new ReservationException(ErrorCode.USER_NOT_FOUND);
    }

    // 인증 이메일을 보냄과 동시에 인증 유효기간은 보낸 시점에서 하루로 설정
    @Transactional
    public LocalDateTime ChangeAdminValidateEmail(Long adminId, String verificationCode) {
        Optional<Admin> adminOptional
                = adminRepository.findById(adminId);
        if(adminOptional.isPresent()){
            Admin admin = adminOptional.get();

            admin.setVerificationCode(verificationCode);
            admin.setVerifyExpiredAt(LocalDateTime.now().plusDays(1));
            return admin.getVerifyExpiredAt();
        }
        throw new ReservationException(ErrorCode.USER_NOT_FOUND);
    }

    // ###############################################################################

    // 고객이 이메일로 받은 인증 url을 클릭하면 @RequestParam 으로 온 email, code 들을 검증하고
    // 고객이 이미 인증되었는 지, 인증 유효 기간이 지났는지 검증
    // 조건들을 다 통과하면 고객을 인증된 상태로 바꿈
    @Transactional
    public void verifyCustomerEmail(String email, String code) {

        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));

        if (!Objects.equals(customer.getVerificationCode(), code)) {
            throw new ReservationException(ErrorCode.WRONG_VERIFICATION);
        }

        if(customer.isVerify()){
            throw new ReservationException(ErrorCode.ALREADY_VERIFY);
        }

        if (customer.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ReservationException(ErrorCode.EXPIRE_CODE);
        }

        customer.setVerify(true);
        customerRepository.save(customer);
    }

    // 점장이 이메일로 받은 인증 url을 클릭하면 @RequestParam 으로 온 email, code 들을 검증하고
    // 점장이 이미 인증되었는 지, 인증 유효 기간이 지났는지 검증
    // 점장을 인증된 상태로 바꿈

    @Transactional
    public void verifySellerEmail(String email, String code) {

        Seller seller = sellerRepository.findByEmail(email)
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));

        if (!Objects.equals(seller.getVerificationCode(), code)) {
            throw new ReservationException(ErrorCode.WRONG_VERIFICATION);
        }

        if(seller.isVerify()){
            throw new ReservationException(ErrorCode.ALREADY_VERIFY);
        }

        if (seller.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ReservationException(ErrorCode.EXPIRE_CODE);
        }

        seller.setVerify(true);
        sellerRepository.save(seller);
    }

    @Transactional
    public void verifyAdminEmail(String email, String code) {
        Admin admin = adminRepository.findByEmail(email)
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));


        if (!Objects.equals(admin.getVerificationCode(), code)) {
            throw new ReservationException(ErrorCode.WRONG_VERIFICATION);
        }

        if(admin.isVerify()){
            throw new ReservationException(ErrorCode.ALREADY_VERIFY);
        }

        if (admin.getVerifyExpiredAt().isBefore(LocalDateTime.now())) {
            throw new ReservationException(ErrorCode.EXPIRE_CODE);
        }

        admin.setVerify(true);
        adminRepository.save(admin);
    }


    // 관리자가 이메일로 받은 인증 url을 클릭하면 @RequestParam 으로 온 email, code 들을 검증하고
    // 관리자가 이미 인증되었는 지, 인증 유효 기간이 지났는지 검증
    // 조건들을 다 통과하면 고객을 인증된 상태로 바꿈

}
