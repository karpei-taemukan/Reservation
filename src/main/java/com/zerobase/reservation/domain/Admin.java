package com.zerobase.reservation.domain;

import com.zerobase.reservation.domain.form.SignUpForm;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.envers.AuditOverride;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@AuditOverride(forClass = BaseEntity.class)
public class Admin extends BaseEntity implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    @NonNull
    private String adminpassword;

    private String roles;

    @Column(unique = true)
    private String name;

    private String phone;
    private boolean verify;

    private LocalDateTime verifyExpiredAt;
    private String verificationCode;

    public static Admin from(SignUpForm form){
        return Admin.builder()
                .email(form.getEmail())
                .name(form.getName())
                .adminpassword(form.getPassword())
                .verify(false)
                .roles(form.getRoles())
                .phone(form.getPhone())
                .build();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(roles));
    }

    @Override
    public String getPassword() {
        return null;
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
