package com.zerobase.reservation.application;

import com.zerobase.reservation.config.MailgunClient;
import com.zerobase.reservation.domain.Admin;
import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.domain.Seller;
import com.zerobase.reservation.domain.form.SignUpForm;
import com.zerobase.reservation.domain.mailgun.SendMailForm;
import com.zerobase.reservation.service.SignUpService;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignUpApplication {

    private final MailgunClient mailgunClient;
    private final SignUpService signUpService;


    //####################################################################


    // 인증 메일에 필요한 랜덤 코드 생성
    private String getRandomCode(){
        // 10 자리의 문자와 숫자가 섞인 랜덤문자
        return RandomStringUtils.random(10,true,true);
    }

    // 인증 메일 내용 만들기

    // (예시)
    // Hello ZeroBase! Please Click Link for verification.
    //http://localhost:8083/signUp/verify/customer?email=~~~@gmail.com&code=ABCD1234

    private String getVerificationEmailBody(String email, String name, String type, String code){
        StringBuilder sb = new StringBuilder();
        return sb.append("Hello ").append(name)
                .append("! Please Click Link for verification. \n")
                .append("http://localhost:8083/signUp")
                .append("/verify/")
                .append(type)
                .append("?email=")
                .append(email)
                .append("&code=")
                .append(code)
                .toString();
    }




    //---------------------------------------------------------------------------------------------



    /*
     * 고객(customer)의 회원 가입과 이메일 인증
     * */

    public String customerSignUp(SignUpForm form){

        // 이미 가입한 이메일 있는 지 확인
        if(signUpService.isCustomerEmailExist(form.getEmail())){
            throw new ReservationException(ErrorCode.ALREADY_SIGNUP);
        }else{

            /*
            * 고객 회원 가입 후
            * 인증 url 이메일로 발송
            * */

            Customer customer = signUpService.signUpCustomer(form);

            String code = getRandomCode();

            // 인증 url 만들기
            SendMailForm verificationEmail = SendMailForm.builder()
                    .from("zerobase-test@email.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(form.getEmail(), form.getName(), "customer" ,code))
                    .build();

            // 이메일 보내기
            mailgunClient.sendEmail(verificationEmail);

            // 인증 이메일을 보냄과 동시에 이메일에서 제공한 코드랑 인증 유효기간 하루로 설정
            signUpService.ChangeCustomerValidateEmail(customer.getId(), code);
            return "회원 가입에 성공";
        }

    }

    /*
    * 인증메일로 온 url 클릭시 검증하기
    * */
    public void customerVerify(String email, String code) {
        signUpService.verifyCustomerEmail(email,code);
    }



    // ###############################################################################


    /*
     * 점장(seller)의 회원 가입과 이메일 인증
     * */

    public String sellerSignUp(SignUpForm form) {
        // 이미 가입한 이메일 있는 지 확인
        if(signUpService.isSellerEmailExist(form.getEmail())){
            throw new ReservationException(ErrorCode.ALREADY_SIGNUP);
        }else{

            /*
             * 점장 회원 가입 후
             * 인증 url 이메일로 발송
             * */

            Seller seller = signUpService.signUpSeller(form);

            System.out.println("SELLER  "+seller);
            System.out.println("SELLER PW  "+seller.getPassword());
            String code = getRandomCode();

            // 인증 url 만들기
            SendMailForm verificationEmail = SendMailForm.builder()
                    .from("zerobase-test@email.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(form.getEmail(), form.getName(), "seller" ,code))
                    .build();


            // 이메일 보내기
            mailgunClient.sendEmail(verificationEmail);


            // 인증 이메일을 보냄과 동시에 이메일에서 제공한 코드랑 인증 유효기간 하루로 설정
            signUpService.ChangeSellerValidateEmail(seller.getId(), code);
            return "회원 가입에 성공";
        }
    }


    /*
     * 인증메일로 온 url 클릭시 검증하기
     * */
    public void sellerVerify(String email, String code) {
        signUpService.verifySellerEmail(email,code);
    }

    public String adminSignUp(SignUpForm form) {
        // 이미 가입한 이메일 있는 지 확인
        if(signUpService.isAdminEmailExist(form.getEmail())){
            throw new ReservationException(ErrorCode.ALREADY_SIGNUP);
        }else{

            /*
             * 관리자 회원 가입 후
             * 인증 url 이메일로 발송
             * */

            System.out.println("ADMIN PW1 "+form.getPassword());

            Admin admin = signUpService.signUpAdmin(form);

            System.out.println("ADMIN  "+admin);
            System.out.println("ADMIN PW2 "+admin.getPassword());

            String code = getRandomCode();

            // 인증 url 만들기
            SendMailForm verificationEmail = SendMailForm.builder()
                    .from("zerobase-test@email.com")
                    .to(form.getEmail())
                    .subject("Verification Email")
                    .text(getVerificationEmailBody(form.getEmail(), form.getName(), "admin" ,code))
                    .build();


            // 이메일 보내기
            mailgunClient.sendEmail(verificationEmail);


            // 인증 이메일을 보냄과 동시에 이메일에서 제공한 코드랑 인증 유효기간 하루로 설정
            signUpService.ChangeAdminValidateEmail(admin.getId(), code);
            return "회원 가입에 성공";
        }
    }


    /*
     * 관리자(ADMIN)의 회원 가입과 이메일 인증
     * */
    public void adminVerify(String email, String code) {
        signUpService.verifyAdminEmail(email,code);
    }


}
