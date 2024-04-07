package com.zerobase.reservation.service;

import com.zerobase.reservation.domain.Customer;
import com.zerobase.reservation.repository.CustomerRepository;
import com.zerobase.reservation.token.util.Aes256Util;
import exception.ErrorCode;
import exception.ReservationException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerService implements UserDetailsService {
    private final CustomerRepository customerRepository;

    // 해당 필터에 맞는 조건이 없다면 Optional 로 리턴
    public Optional<Customer> findValidCustomer(String email, String password){
        return customerRepository.findByEmail(email).stream().filter(
                customer -> customer.getPassword().equals(password) && customer.isVerify()
        ).findFirst();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        System.out.println("CUSTOMER NAME: "+username);

        String name = Aes256Util.decrypt(username);

        return customerRepository.findByName(name)
                .orElseThrow(()-> new UsernameNotFoundException("Couldn't find user -> " + username));
    }

}
