package com.zerobase.reservation.dto;

import com.zerobase.reservation.domain.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDto {
    private Long id;
    private String name;
    private String password;
    private String phone;

    public static CustomerDto from(Customer customer){
        return CustomerDto.builder()
                .id(customer.getId())
                .name(customer.getName())
                .password(customer.getPassword())
                .phone(customer.getPhone())
                .build();
    }
}