package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Admin;
import com.zerobase.reservation.repository.AdminRepository;
import com.zerobase.reservation.token.util.Aes256Util;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminService implements UserDetailsService {

    private final AdminRepository adminRepository;

 public Optional<Admin> findValidAdmin(String email, String password){
     return adminRepository.findByEmail(email).stream().filter(
             admin -> admin.getAdminpassword().equals(password) && admin.isVerify()
     ).findFirst();
 }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        String name = Aes256Util.decrypt(username);

        return adminRepository.findByName(name)
                .orElseThrow(()-> new UsernameNotFoundException("Couldn't find user -> " + username));
    }
}
