package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Review;
import com.zerobase.reservation.domain.form.ReviewForm;
import com.zerobase.reservation.dto.ReviewDto;
import com.zerobase.reservation.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
@Tag(name = "Review-Controller", description = "고객의 리뷰 작성, 수정, 삭제")
public class ReviewController {

    private final ReviewService reviewService;

    public static final String TOKEN_PREFIX = "Bearer ";

    /*
    * CUSTOMER 권한을 가지고 있을 경우만
    * 리뷰 작성 가능
    * */
    @PostMapping("/write")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "고객의 리뷰 작성")
    public ResponseEntity<ReviewDto> writeReview(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ReviewForm form
    ){

        token = token.substring(TOKEN_PREFIX.length());

        Review review = reviewService.writeReview(token, form);

        ReviewDto reviewDto = ReviewDto.from(review);
        return ResponseEntity.ok(reviewDto);
    }



    /*
     * CUSTOMER 권한을 가지고 있을 경우만
     * 리뷰 수정 가능
     * */
    @PutMapping("/modify")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "고객의 리뷰 수정")
    public ResponseEntity<ReviewDto> modifyReview(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ReviewForm form
    ){
        token = token.substring(TOKEN_PREFIX.length());

        Review review = reviewService.modifyReview(token, form);

        ReviewDto reviewDto = ReviewDto.from(review);

        return ResponseEntity.ok(reviewDto);
    }



    /*
     /*
     * CUSTOMER 권한을 가지고 있을 경우, ADMIN 권한을 가지고 있을 경우에만
     * 리뷰 삭제 가능
     *
    * 고객은 여러 매장에 리뷰를 작성할 수 있다
    * 그래서 특정 가게이름을 RequestParam 으로 받는다
    * */

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @Operation(summary = "고객 혹은 관리자의 리뷰 삭제")
    public ResponseEntity<String> deleteReview(
            @RequestHeader(name = "Authorization") String token,
            @RequestParam(name = "store") String storeName
    ){

        token = token.substring(TOKEN_PREFIX.length());

        reviewService.deleteReview(token, storeName);
        return ResponseEntity.ok("등록한 리뷰가 삭제됨");
    }

}