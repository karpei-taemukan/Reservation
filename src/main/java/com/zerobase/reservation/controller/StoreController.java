package com.zerobase.reservation.controller;

import com.zerobase.reservation.domain.form.StoreForm;
import com.zerobase.reservation.dto.StoreDto;
import com.zerobase.reservation.service.StoreService;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserVo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

//  매장 등록, 수정, 삭제 기능 구현
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/store")
@Tag(name = "Store-Controller", description = "점장의 매장 등록, 수정, 삭제")
public class StoreController {

    private final StoreService storeService;
    private final JwtAuthProvider provider;
    public static final String TOKEN_PREFIX = "Bearer ";

    // ########################################################################################

    // 점장으로 로그인된 상태에서 매장 등록
    // 점장의 권한을 가졌을때만 매장 등록 가능
    @PostMapping("/register")
    @PreAuthorize("hasRole('SELLER')")
    @Operation(summary = "점장의 매장 등록")
    public ResponseEntity<String> registerStore(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody StoreForm form
            ){

        token = token.substring(TOKEN_PREFIX.length());

/*        System.out.println("TOKEN "+ token);
        System.out.println("FORM "+form);*/

        storeService.registerStore(form, token);
        return ResponseEntity.ok("매장 등록됨");
    }


    // ########################################################################################


    // 점장이 자신의 매장 정보를 수정할 경우
    // 점장의 권한을 가졌을때만 매장 수정 가능
    @PutMapping("/modify")
    @PreAuthorize("hasRole('SELLER')")
    @Operation(summary = "점장의 등록한 매장 수정")
    public ResponseEntity<StoreDto> modifyStore(
            @RequestHeader(name = "Authorization") String token,
            @RequestBody StoreForm form
    ){


        token = token.substring(TOKEN_PREFIX.length());

        UserVo vo = provider.getUserVo(token);

        return ResponseEntity.ok(StoreDto.from(storeService.modifyStore(vo.getId(), form)));
    }


    // ########################################################################################

    // 점장이 자신의 매장 정보를 수정할 경우거나 관리자가 등록한 매장을 삭제하는 경우
    // 점장의 권한 혹은 관리자의 권한을 가졌을때만 매장 정보 삭제 가능
    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyRole('ADMIN','SELLER')")
    @Operation(summary = "점장 혹은 관리자의 등록한 매장 삭제")
    public ResponseEntity<String> deleteStore(
            @RequestHeader(name = "Authorization") String token
    ){
        // sellerId로 seller 가 등록한 매장 찾기


        token = token.substring(TOKEN_PREFIX.length());

        storeService.deleteStore(provider.getUserVo(token).getId());
        return ResponseEntity.ok("등록한 매장이 삭제됨");
    }
}