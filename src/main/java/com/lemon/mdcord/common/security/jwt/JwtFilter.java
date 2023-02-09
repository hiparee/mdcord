package com.lemon.mdcord.common.security.jwt;

import com.lemon.mdcord.common.exception.UserAuthenticationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    /**
     * OncePerRequestFilter - Http request 요청 시 1번만 검증하기 위함
     * JWT 검증 로직 실패 시 SecurityContextHolder 초기화
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = jwtProvider.resolveToken(request);
        try {
            if(StringUtils.hasText(token) && jwtProvider.validateToken(token)) {
                Authentication authentication = jwtProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        catch(UserAuthenticationException e) {
            SecurityContextHolder.clearContext();
            throw new UserAuthenticationException(e.getMessage(), e);
        }
        catch(Exception e) {
            log.error("JwtFilter doFilterInternal Exception - " + e.getMessage());
            SecurityContextHolder.clearContext();
            return;
        }

        filterChain.doFilter(request, response);
    }

}
