package com.zerobase.reservation.dto;

import com.zerobase.reservation.domain.Reserve;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ReserveDto {
    private String storeName;
    private String customerName;
    // 예약할 날짜
    private String reserveDate;
    // 예약할 시간
    private String reserveTime;


    public static ReserveDto from(Reserve reserve){
        return ReserveDto.builder()
                .storeName(reserve.getStoreName())
                .customerName(reserve.getCustomerName())
                .reserveDate(reserve.getReserveDate())
                .reserveTime(reserve.getReserveTime())
                .build();
    }
}
