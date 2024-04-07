package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.Reserve;
import com.zerobase.reservation.domain.form.ReserveForm;
import com.zerobase.reservation.dto.ReserveDto;
import com.zerobase.reservation.service.ReserveService;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reserve")
@Tag(name = "Reserve-Controller", description = "고객의 예약")
public class ReserveController {

    private final ReserveService reserveService;
    private final JwtAuthProvider provider;
    public static final String TOKEN_PREFIX = "Bearer ";


    // CUSTOMER 권한을 가진 사용자만이 예약 가능
    /*
    * 토큰에 있는 정보 중 고객의 이름을 이용
    * 고객의 이름을 이용하는 이유는  @Column(unique = true) 으로 설정했기 때문임
    * */
    @PostMapping
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "고객의 매장 예약")
    public ResponseEntity<ReserveDto> reserveStore(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ReserveForm reserveForm
            )
    {
        token = token.substring(TOKEN_PREFIX.length());

        // 토큰에 있는 정보 가져오기
        UserVo vo = provider.getUserVo(token);

        System.out.println("NAME: "+vo.getName());

        System.out.println("STORE NAME: "+reserveForm.getStoreName());

        /*
        * token 에 예약하는 고객의 이름을 가져온다
        * */

        Reserve reserve = reserveService.reserveStore(vo.getName(), reserveForm);

        ReserveDto reserveDto = ReserveDto.from(reserve);

        return ResponseEntity.ok(reserveDto);
    }



    //##########################################################################################



    /*
       CUSTOMER 권한을 가진 사용자만이 예약 취소 가능
       예약 취소
    */

    @DeleteMapping("/cancel")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "고객의 매장 예약 취소")
    public ResponseEntity<String> cancelReserveStore(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody ReserveForm reserveForm
    ){
        token = token.substring(TOKEN_PREFIX.length());

        UserVo vo = provider.getUserVo(token);

        reserveService.cancelReserveStore(vo.getName(), reserveForm);

        return ResponseEntity.ok("예약 취소 완료됨");
    }

}