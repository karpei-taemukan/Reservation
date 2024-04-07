package com.zerobase.reservation.dto;

import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.domain.form.StoreForm;
import com.zerobase.reservation.type.StoreReserveType;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class StoreDto {

    private String name;

    private Long sellerId;

    private String location;

    private String description;

    private LocalDateTime reserveTime;


    public static StoreDto from(Store store){
        return StoreDto.builder()
                .name(store.getName())
                .sellerId(store.getSellerId())
                .location(store.getLocation())
                .description(store.getDescription())
                .build();
    }
}