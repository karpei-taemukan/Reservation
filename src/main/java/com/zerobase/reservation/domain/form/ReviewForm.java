package com.zerobase.reservation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReviewForm {
    @Schema(description = "매장 이름", example = "홍길동 분식")
    private String storeName;
    @Schema(description = "고객 리뷰", example = "너무 맛나요")
    private String review;
}