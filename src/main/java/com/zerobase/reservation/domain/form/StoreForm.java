package com.zerobase.reservation.domain.form;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

/* 점장(seller) 가 등록(register)할 수 있는 정보는 가게 이름, 가게 위치, 가게 설명 정보이다
*  또한 점장이 3가지 정보로 가게를 등록했기 때문에
*  점장은 가게의 정보를 수정(modify)할때는 3가지 정보만 수정 가능하다
* */

@Getter
public class StoreForm {
    @Schema(description = "매장 이름", example = "홍길동 분식")
    private String name;
    @Schema(description = "매장 위치", example = "경기도 ~시 ~동 ~길 1234")
    private String location;
    @Schema(description = "매장 소개", example = "메뉴: 떡볶이, 튀김, 순대 등,, 5000원 이상시 서비스 등,,")
    private String description;
}
