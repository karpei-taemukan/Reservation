package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.domain.form.KioskForm;
import com.zerobase.reservation.repository.CustomerRepository;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserVo;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class KioskService {

    private final CustomerRepository customerRepository;


    /*
    * 고객 repository 에서 고객이름으로 고객을 찾아, 고객이 예약한 시간을 찾는다
    * */

    public String arrivalCheck(String username/*(토큰에 있는 이름 정보)*/, KioskForm kioskForm) {


        System.out.println("ARRIVAL CUSTOMER NAME: "+username);

        // CustomerRepository 에서 고객이 있는 지 체크
        Customer customer = customerRepository.findByName(username)
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));


        // 토큰에 있는 이름 정보와 CustomerRepository 에서 찾은 이름 정보가 일치하는 지 체크
        // 이름으로만 체크를 하는 이유는 @Column(unique = true) 이기 때문이다

        /* kioskForm 에 있는 고객 이름과 이메일 들이 CustomerRepository 에 있는 이름과 이메일
         * 하나라도 일치하지않을 경우
         */
        if(!kioskForm.getCustomerName().equals(customer.getName())
                || !kioskForm.getCustomerEmail().equals(customer.getEmail())){
            throw new ReservationException(ErrorCode.USER_NOT_FOUND);
        }


        /*
        * 예를 들면,
        * KioskForm 에서 온 customerReserveDate 은
        * "2024-3-30"과 같은 문자열이기 때문에 LocalDate 형으로 변환한다
        *
        * KioskForm 에서 온 customerReserveTime 은
        * "17:00" 과 같은 문자열이기 때문에 LocalTime 형으로 변환한다
        * */

        String customerReserveDate = customer.getReserveDate();
        String customerReserveTime = customer.getReserveTime();

        LocalDate reserveDate = LocalDate.parse(customerReserveDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalTime reserveTime = LocalTime.parse(customerReserveTime, DateTimeFormatter.ofPattern("HH:mm"));

        LocalDate kioskDate = LocalDate.now();
        LocalTime kioskTime = LocalTime.now();


        /*
         *  예약시간 10분전에 도착해야만 예약가능
         *
         *  --> 키오스크에서 도착 확인한 시간 == 예약시간 - 10 분
         *  위의 수식이 성립해야 고객이 10분전에 도착 한 것이다
         *
         *  만약 예약시간 10분 이전에 도착한 경우(11분전에 도착을 한다던지 아님 30분 전에 도착을 한다던지 등..)
         *  예를 들어 11분전에 도착하면 "1분후에 키오스크에서 도착확인을 하세요" 같은 메세지를 준다
         * */

        // 예약한 날짜랑 kiosk에서 도착확인한 날짜가 같을 때 예약이 가능해야한다
        if(reserveDate.equals(kioskDate)) {

            // 키오스크에서 도착 확인한 시간과 (예약시간 - 10 분) 사이에 시간 차이
            Duration difference = Duration.between(reserveTime.minusMinutes(10), kioskTime);

            // 키오스크에서 도착 확인한 시간 == 예약시간 - 10 분
            // (예약시간 - 10 분) 이 될때 키오스크에서 도착확인한 경우 (예약 성공)
            // 예)    6:00 == 6:10 - 10분  --> 예약 성공
            // --> 6:00:00 ~ 6:00:59 까지는 예약이 성공하게 함
            if (difference.toSeconds() >= 0 && difference.toSeconds() <= 59) {
                return "예약 성공";
            }

            // 키오스크에서 도착 확인한 시간 < 예약시간 - 10 분
            // (예약시간 - 10 분) 이 되기 전에 키오스크에서 도착확인한 경우 (너무 일찍 도착한 경우)
            // 예) 6:00 예약함 ---> 5:50까지는 와야하나 5:40에 도착해서 키오스크에 도착확인한 경우
            // "10분 00초 후에 다시 키오스크에서 도착확인을 하세요" 같은 메세지를 준다
            else if (difference.toSeconds() < 0) {
                return -difference.toSeconds()/60+"분 "+-difference.toSeconds()%60+"초 후에 다시 키오스크에서 도착확인을 하세요";
            }

            // 키오스크에서 도착 확인한 시간 > 예약시간 - 10 분
            // 10분전이 아닌 그 이후에 키오스크 도착확인을 한 경우 (늦게 온 경우)
            // 예) 6:00 예약함 --> 5:50까지는 와야하나 5:55에 와서 키오스크에 도착확인한 경우
            else {
                return "예약시간 10분전에 도착 못함 --> 예약 불가";
            }

        }else{
            return "예약한 날짜와 키오스크에서 도착확인한 날짜가 다릅니다";
        }
    }
}