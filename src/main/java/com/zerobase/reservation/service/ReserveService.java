package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.domain.Reserve;
import com.zerobase.reservation.domain.form.ReserveForm;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.repository.CustomerRepository;
import com.zerobase.reservation.repository.ReserveRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.type.CustomerReserveType;
import com.zerobase.reservation.type.StoreReserveType;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReserveService {

    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ReserveRepository reserveRepository;

    /*
    * 예약할 storeName 을 찾아, 예약 상태 변경
    *
    * 예약할 customerName 으로 고객을 찾아, 예약 가능 상태인지 확인
    * */

    @Transactional
    public Reserve reserveStore(String customerName, ReserveForm form) {

        System.out.println("NAME: "+customerName);

        System.out.println("STORE NAME: "+form.getStoreName());


        Customer customer = customerRepository.findByName(customerName)
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));

        // 이미 예약한 고객인지 판별
        if(customer.getReserveStatus().equals(CustomerReserveType.YES_RESERVE)){
            throw new ReservationException(ErrorCode.ALREADY_RESERVE);
        }

        //
        Store store = storeRepository.findByName(form.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_STORE));

        // 이미 예약된 매장인지 판별
        if(store.getEnableReserve().equals(StoreReserveType.RESERVE_IMPOSSIBLE)){
            throw new ReservationException(ErrorCode.ALREADY_RESERVED_STORE);
        }

        // 고객은 예약상태 변경 후,
        // 예약 날짜 시간 은 ReserveForm 에서 넘어온 것으로 변경
        // 예약 매장은 예약불가로 바꿈
        customer.setReserveStatus(CustomerReserveType.YES_RESERVE);
        customer.setReserveDate(form.getReserveDate());
        customer.setReserveTime(form.getReserveTime());
        store.setEnableReserve(StoreReserveType.RESERVE_IMPOSSIBLE);



        /*
        * ReserveRepository 에 예약 정보 저장
        * */

        Reserve reserve = Reserve.builder()
                .storeName(store.getName())
                .customerName(customer.getName())
                .reserveDate(customer.getReserveDate())
                .reserveTime(customer.getReserveTime())
                .build();

        reserveRepository.save(reserve);

        return reserve;
    }

    //##########################################################################################

    /*
    * ReserveRepository 에 있는 특정 고객의 특정 매장의 예약 내역을 찾아 삭제
    * --> 예약 취소
    * */

    @Transactional
    public void cancelReserveStore(String name, ReserveForm reserveForm) {
        Customer customer = customerRepository.findByName(name)
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));
        Store store = storeRepository.findByName(reserveForm.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_STORE));

        /*
        * 고객과 고객이 예약한 매장을 예약 전의 상태로 변경
        * */

        customer.setReserveStatus(CustomerReserveType.NO_RESERVE);
        customer.setReserveDate(null);
        customer.setReserveTime(null);
        store.setEnableReserve(StoreReserveType.RESERVE_POSSIBLE);


        Reserve reserve = reserveRepository.findByCustomerNameAndStoreName(name, reserveForm.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_RESERVED));

        reserveRepository.delete(reserve);


    }
}
