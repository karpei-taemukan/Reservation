package com.zerobase.reservation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
public class ReserveForm {
    @Schema(description = "매장 이름", example = "길동이네 분식")
    private String storeName;
    @Schema(description = "매장 예약 날짜", example = "2024-04-05")
    private String reserveDate;
    @Schema(description = "매장 예약 시간", example = "17:25")
    private String reserveTime;
}
