package com.zerobase.reservation.token.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserVo {
    private Long id;
    private String name;
    private String userType;
}
