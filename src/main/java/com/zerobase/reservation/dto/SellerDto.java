package com.zerobase.reservation.dto;

import com.zerobase.reservation.domain.Seller;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SellerDto {
    private Long id;
    private String password;
    private String name;

    public static SellerDto from(Seller seller){
        return SellerDto.builder()
                .id(seller.getId())
                .name(seller.getName())
                .password(seller.getPassword())
                .build();
    }
}
