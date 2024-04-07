package com.zerobase.reservation.service;


import com.zerobase.reservation.domain.Seller;
import com.zerobase.reservation.repository.SellerRepository;
import com.zerobase.reservation.token.util.Aes256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SellerService implements UserDetailsService {
    private final SellerRepository sellerRepository;

    // 해당 필터에 맞는 조건이 없다면 Optional 로 리턴
    public Optional<Seller> findValidSeller(String email, String password){
        return sellerRepository.findByEmail(email).stream().filter(
                seller -> seller.getPassword().equals(password) && seller.isVerify()
        ).findFirst();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("SELLER USERNAME   "+ username);

        String name = Aes256Util.decrypt(username);
        System.out.println("SELLER USERNAME   "+ name);
        return sellerRepository.findByName(name)
                .orElseThrow(()-> new UsernameNotFoundException("Couldn't find user -> " + username));
    }

}
