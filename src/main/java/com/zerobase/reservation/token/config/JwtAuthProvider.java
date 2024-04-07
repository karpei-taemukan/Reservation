package com.zerobase.reservation.token.config;

import com.zerobase.reservation.service.AdminService;
import com.zerobase.reservation.service.CustomerService;
import com.zerobase.reservation.service.SellerService;
import com.zerobase.reservation.token.domain.UserType;

import com.zerobase.reservation.token.domain.UserVo;
import com.zerobase.reservation.token.util.Aes256Util;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Objects;


@Component
@RequiredArgsConstructor
public class JwtAuthProvider {
    private static final String secretKey = "SecretKey";
    // 토큰 유효 기간 (하루)
    private static final Long tokenValidTime = 1000L * 60 * 60 * 24;


    private final CustomerService customerService;

    private final SellerService sellerService;

    private final AdminService adminService;


    // 토큰 발행하기
    public String createToken(String name, Long id, UserType userType){

        /*
         * jwt 는 누구나 디코딩이 가능해서 민감한 정보는 한번 더 암호화를 거친다
         *
         * 그래서 아래의 코드처럼 userPk 이나 secretKey 들을 암호화를 한다
         * */

        Claims claims
                = Jwts.claims()
                .setSubject(Aes256Util.encrypt(name))
                .setId(Aes256Util.encrypt(id.toString()))
                .setAudience(Aes256Util.encrypt(userType.toString()));

        claims.put("roles", userType);

        //System.out.println("ROLES   "+  claims.get("roles"));

        Date now = new Date();

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + tokenValidTime))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    // JWS 를 이용해 JWT 토큰의 유효성 확인
    public boolean validateToken(String jwtToken){
        try{
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(jwtToken);
            return !claimsJws.getBody().getExpiration().before(new Date());
        }catch (Exception e){
            return false;
        }
    }

    // 토큰에 설정해둔 정보를 꺼낼 수 있도록한 메소드
    public UserVo getUserVo(String token){
        Claims claims = Jwts.parser().setSigningKey(secretKey)
                .parseClaimsJws(token).getBody();

        System.out.println("ID: "+ claims.getId());
        System.out.println("SUBJECT: "+claims.getSubject());
        System.out.println("CLAIMS: "+ claims.getAudience());

        return new UserVo(Long.valueOf(Objects.requireNonNull(Aes256Util.decrypt(claims.getId()))),
                Aes256Util.decrypt(claims.getSubject()), Aes256Util.decrypt(claims.getAudience()));
    }

    // 토큰 파싱
    private Claims parseClaims(String token){
        try {
            return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        }catch (ExpiredJwtException e){
            return e.getClaims();
        }
    }

    // 토큰을 파싱해서 토큰안 이름 정보 가져오기
    public String getUsername(String token){
        return this.parseClaims(token).getSubject();
    }

    // JWT 토큰으로부터 인증 정보를 가져오는 메소드(고객)
    public Authentication getCustomerAuthentication(String jwt) {
        UserDetails userDetails
                = customerService.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );

    }


    // JWT 토큰으로부터 인증 정보를 가져오는 메소드(점장)
    public Authentication getSellerAuthentication(String jwt) {
        UserDetails userDetails
                = sellerService.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );

    }


    // JWT 토큰으로부터 인증 정보를 가져오는 메소드(관리자)
    public Authentication getAdminAuthentication(String jwt) {
        UserDetails userDetails
                = adminService.loadUserByUsername(getUsername(jwt));
        return new UsernamePasswordAuthenticationToken(
                userDetails,
                "",
                userDetails.getAuthorities()
        );
    }
}
