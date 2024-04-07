package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.form.KioskForm;
import com.zerobase.reservation.service.KioskService;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/kiosk")
@RequiredArgsConstructor
@Tag(name = "Kiosk-Controller", description = "고객이 도착확인할 키오스크")
public class KioskController {

    private final KioskService kioskService;
    private final JwtAuthProvider provider;

    public static final String TOKEN_PREFIX = "Bearer ";



    // 예약한 손님이 매장에 도착해서 키오스크로 도착 확인을 하는 경우

    // (ReserveTime - 10분) 시간과 키오스크에서 도착했을떄 시간을 비교

    // 손님의 권한일 때만 도착확인 가능
    @PostMapping("/arrival")
    @PreAuthorize("hasRole('CUSTOMER')")
    @Operation(summary = "고객의 도착확인")
    public ResponseEntity<String> arrival(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody KioskForm kioskForm
            ){
        token = token.substring(TOKEN_PREFIX.length());

        UserVo vo = provider.getUserVo(token);

        System.out.println("ARRIVAL CUSTOMER NAME: "+vo.getName());

        String msg = kioskService.arrivalCheck(vo.getName(), kioskForm);
        return ResponseEntity.ok(msg);
    }
}