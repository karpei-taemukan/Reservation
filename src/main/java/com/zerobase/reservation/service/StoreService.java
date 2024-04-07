package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Seller;
import com.zerobase.reservation.domain.Store;
import com.zerobase.reservation.domain.form.StoreForm;
import com.zerobase.reservation.repository.SellerRepository;
import com.zerobase.reservation.repository.StoreRepository;
import com.zerobase.reservation.token.config.JwtAuthProvider;
import com.zerobase.reservation.token.domain.UserVo;
import com.zerobase.reservation.type.StoreReserveType;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class StoreService {
    private final SellerRepository sellerRepository;
    private final StoreRepository storeRepository;
    private final JwtAuthProvider provider;


    // 점장(seller)의 매장 등록
    public void registerStore(StoreForm form, String token) {

        System.out.println("TOKEN "+ token);
        System.out.println("FORM "+form);

        // 토큰안 정보 가져오기
        UserVo vo = provider.getUserVo(token);

        // 토큰에 있는 정보(점장(seller) id, 점장(seller) email)를 통해 점장(seller) repository 에 저장되있는 지 확인
        boolean isExist = storeRepository.existsById(vo.getId());

        // 이미 등록한 매장이 있는 경우
        if(isExist){
            throw new ReservationException(ErrorCode.ALREADY_REGISTERED_STORE);
        }

        Store store = Store.builder()
                .name(form.getName())
                .sellerId(vo.getId()) // 점장(seller) id
                .enableReserve(StoreReserveType.RESERVE_POSSIBLE) // 매장을 처음 등록했기 때문에 예약 가능
                .description(form.getDescription())
                .location(form.getLocation())
                .build();

        storeRepository.save(store);
    }


    // ########################################################################################

    // 점장(seller)의 매장 정보 수정
    // StoreForm 에 입력된 정보를 StoreDto 에 저장
    @Transactional
    public Store modifyStore(Long sellerId, StoreForm form) {

        Store sellerStore = storeRepository.findBySellerId(sellerId)
                .orElseThrow(() -> new ReservationException(ErrorCode.NOT_FOUND_STORE));

        sellerStore.setName(form.getName());
        sellerStore.setLocation(form.getLocation());
        sellerStore.setDescription(form.getDescription());
        storeRepository.save(sellerStore);
        return sellerStore;
    }


    // ########################################################################################

    // 점장(seller)의 매장 정보 삭제
    // 토큰안에 있는 점장(seller)의 id를 찾는다 이유는 StoreRepository 에 점장(seller)의 id로 등록된 매장을 찾기위함
    @Transactional
    public void deleteStore(Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(()->new ReservationException(ErrorCode.USER_NOT_FOUND));

        storeRepository.deleteById(seller.getId());
    }



}