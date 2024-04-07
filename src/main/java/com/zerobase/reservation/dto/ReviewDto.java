package com.zerobase.reservation.dto;

import com.zerobase.reservation.domain.Review;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReviewDto {

    private String storeName;
    private String customerName;
    private String review;

    public static ReviewDto from(Review review){
        return ReviewDto.builder()
                .storeName(review.getStoreName())
                .customerName(review.getCustomerName())
                .review(review.getReview())
                .build();
    }

}
