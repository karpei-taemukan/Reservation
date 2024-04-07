package com.zerobase.reservation.domain;

import com.zerobase.reservation.domain.form.SignUpForm;
import com.zerobase.reservation.type.CustomerReserveType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

import static com.zerobase.reservation.type.CustomerReserveType.NO_RESERVE;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Customer extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NonNull
    private String password;

    private String roles;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String name;

    private String phone;
    private boolean verify;

    // 예약할 날짜
    private String reserveDate;
    // 예약할 시간
    private String reserveTime;

    /*
    * 예약이 된 고객인지 판별
    *
    * 예약됨 : YES_RESERVE
    * 예약안됨 : NO_RESERVE
    * */
    @Enumerated(EnumType.STRING)
    private CustomerReserveType reserveStatus;
    private LocalDateTime verifyExpiredAt;
    private String verificationCode;
    public static Customer from(SignUpForm form){
    return Customer.builder()
                        .email(form.getEmail())
                        .phone(form.getPhone())
                        .name(form.getName())
                        .password(form.getPassword())
                        .reserveStatus(NO_RESERVE)
                        .verify(false)
                        .reserveDate(null)
                        .reserveTime(null)
                        .roles(form.getRoles())
                        .build();
    }

    // Spring Security에서 사용자의 권한 정보를 제공하기 위한 메서드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(this.roles));
    }
    @Override
    public String getUsername() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return false;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }
}