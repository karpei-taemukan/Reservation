package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.domain.form.ReviewForm;
import com.zerobase.reservation.repository.CustomerRepository;
import com.zerobase.reservation.repository.ReserveRepository;
import com.zerobase.reservation.repository.ReviewRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserVo;
import com.zerobase.reservation.type.CustomerReserveType;
import com.zerobase.reservation.type.StoreReserveType;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ReserveRepository reserveRepository;
    private final ReviewRepository reviewRepository;
    private final JwtAuthProvider provider;


    // 고객의 리뷰 작성하기
    @Transactional
    public Review writeReview(String token, ReviewForm form) {
        /*
        * 토큰에 있는 이름 정보가 CustomerRepository 에 존재하는 지 체크
        * */

        UserVo vo = provider.getUserVo(token);

        Customer customer = customerRepository.findByName(vo.getName())
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));

        /*
        * StoreRepository 에 form 에 있는 storeName 이 있는 지 체크
        *  */

        Store store = storeRepository.findByName(form.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_STORE));



        /*
        * customerName 과 storeName 이 ReserveRepository 에 있는 지 체크 (예약했는 지 확인)
        * */

        reserveRepository.findByCustomerNameAndStoreName(vo.getName(), store.getName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_RESERVED));


        Review review = Review.builder()
                .storeName(form.getStoreName())
                .customerName(vo.getName())
                .review(form.getReview())
                .build();


        // 리뷰 작성하면 가게예약을 한 고객은 예약한 상태에서 예약이 없는 상태로 바꾼다
        customer.setReserveStatus(CustomerReserveType.NO_RESERVE);
        // 고객이 예약을 하고 매장을 다 이용하고 리뷰를 쓰기 때문에 매장은 다시 예약 가능한 상태로 바꾼다
        store.setEnableReserve(StoreReserveType.RESERVE_POSSIBLE);


        reviewRepository.save(review);
        return review;
    }



    /*
    * 수정할 리뷰 찾기 (고객 이름, 가게이름 으로 ReviewRepository 에 저장한 리뷰 찾기)
    * */

    @Transactional
    public Review modifyReview(String token, ReviewForm form) {
        UserVo vo = provider.getUserVo(token);


        Review review = reviewRepository.findByCustomerNameAndStoreName(vo.getName(), form.getStoreName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_REVIEW));

        // 리뷰 내용 수정
        review.setReview(form.getReview());

        reviewRepository.save(review);

        return review;
    }

    /*
     * 고객은 여러 매장에 리뷰를 작성할 수 있다
     * 그래서 그중 특정 매장에 대한 리뷰를 찾아야한다
     * */
    @Transactional
    public void deleteReview(String token, String storeName) {

        /*
        * 토큰에 있는 사용자 이름 customerRepository 에서 가져오기
        * */

        UserVo vo = provider.getUserVo(token);

        Customer customer = customerRepository.findById(vo.getId())
                .orElseThrow(() -> new ReservationException(ErrorCode.USER_NOT_FOUND));

        Store store = storeRepository.findByName(storeName)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_STORE));



        Review review = reviewRepository.findByCustomerNameAndStoreName(customer.getName(), store.getName())
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_REVIEW));

        reviewRepository.delete(review);
    }
}