package com.zerobase.reservation.config.filter;

import com.zerobase.reservation.token.config.JwtAuthProvider;
import io.micrometer.common.lang.NonNullApi;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
//@WebFilter(urlPatterns = "/customer/*")
@RequiredArgsConstructor
@Slf4j
@NonNullApi
public class JwtFilter extends OncePerRequestFilter {


    public static final String TOKEN_HEADER = "Authorization";

    public static final String TOKEN_PREFIX = "Bearer ";

    private final JwtAuthProvider provider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String token = resolveTokenFromRequest(request);

        /*
        * token 에 있는 userType 에 따라 CUSTOMER, SELLER, ADMIN 에 따라
        * 토큰 유효성 검사
        * */


        if(StringUtils.hasText(token) && provider.validateToken(token) && provider.getUserVo(token).getUserType().equals("CUSTOMER")){
            Authentication authentication = provider.getCustomerAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(String.format("[%s] -> %s", provider.getUsername(token), request.getRequestURI()));
        }
        else if(StringUtils.hasText(token) && provider.validateToken(token) && provider.getUserVo(token).getUserType().equals("SELLER")){
            Authentication authentication = provider.getSellerAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(String.format("[%s] -> %s", provider.getUsername(token), request.getRequestURI()));
        }
        else if(StringUtils.hasText(token) && provider.validateToken(token) && provider.getUserVo(token).getUserType().equals("ADMIN")){
            Authentication authentication = provider.getAdminAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info(String.format("[%s] -> %s", provider.getUsername(token), request.getRequestURI()));
        }
        filterChain.doFilter(request,response);
    }


    private String resolveTokenFromRequest(HttpServletRequest request){
        String token = request.getHeader(TOKEN_HEADER);

        if(!ObjectUtils.isEmpty(token) && token.startsWith(TOKEN_PREFIX)){
            System.out.println("BEFORE TOKEN "+token);
            System.out.println("AFTER TOKEN "+token.substring(TOKEN_PREFIX.length()));
            return token.substring(TOKEN_PREFIX.length());
        }
        return null;
    }
}