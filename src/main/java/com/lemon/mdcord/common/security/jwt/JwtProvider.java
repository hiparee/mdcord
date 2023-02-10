package com.lemon.mdcord.common.security.jwt;

import com.lemon.mdcord.common.exception.UserAuthenticationException;
import com.lemon.mdcord.service.member.JpaMemberDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final String header;
    private final Key key;
    private final long validityMilliSeconds;
    private final UserDetailsService userDetailsService;
    private final SignatureAlgorithm signatureAlgorithm;

    public JwtProvider(
            @Value("${jwt.secret}") String secret
            , @Value("${jwt.header}") String header
            , @Value("${jwt.validity-in-seconds}") long validityMilliSeconds
            , JpaMemberDetailsService memberDetailsService) {
        this.header = header;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.validityMilliSeconds = validityMilliSeconds * 1000;
        this.userDetailsService = memberDetailsService;
        this.signatureAlgorithm = SignatureAlgorithm.HS256;
    }

    /**
     * 사용자 ID와 권한을 Claims에 담아 JWT 생성
     * @param memberId
     * @param MemberRole
     * @return
     */
    public String createToken(String memberId, String MemberRole) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + this.validityMilliSeconds);

        Map<String, Object> claims = new HashMap<>();
        claims.put("member", memberId);
        claims.put("role", MemberRole);

        return Jwts.builder()
                .setClaims(claims)
                .signWith(key, signatureAlgorithm)
                .setIssuedAt(now)
                .setExpiration(expiration)
                .compact();
    }

    /**
     * 정해진 Http Headers의 값을 가져와서 토큰을 확인
     * @param request
     * @return 토큰 앞에 "Bearer "가 붙어있지 않다면 불완전한 토큰으로 판단하여 null
     */
    public String resolveToken(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if (cookie.getName().equals(header)) {
                    return cookie.getValue();
                }
            }
        }
//        String bearerToken = request.getHeader(header);
//        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
//        if(StringUtils.hasText(bearerToken)) {
//            return bearerToken;
//            return bearerToken.substring(7);
//        }
        return null;
    }

    /**
     * 토큰 검증
     * resolveToken()에서 얻은 토큰을 검증
     * @param token
     * @return 토큰의 만료 시간과 현재 시간을 비교
     *         현재 시간이 만료 시간을 넘겼다면 false
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // TODO - refresh token을 만든다면 아마도 여기서 만들어야 할듯

            return !claims.getExpiration().before(new Date());
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT.");
            throw new UserAuthenticationException("Throw expired token exception", e);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT.");
            log.error("token : {}", token);
            throw new UserAuthenticationException("Throw unsupported token exception", e);
        } catch (ClaimJwtException e) {
            log.info("Not Expected JWT claims.");
            log.error("token : {}", token);
            throw new UserAuthenticationException("Throw not expected token exception", e);
        } catch (IllegalArgumentException e) {
            log.info("IllegalArgument JWT.");
            log.error("token : {}", token);
            throw new UserAuthenticationException("Throw illegal token exception", e);
        } catch (MalformedJwtException e) {
            log.info("Invalid JWT.");
            log.error("token : {}", token);
            throw new UserAuthenticationException("Throw invalid token exception", e);
        } catch (Exception e) {
            log.info("validateToken Exception.");
            log.error("token : {}", token);
            log.error("Exception Message : {}", e.getMessage());
            throw new UserAuthenticationException("Throw exception", e);
        }
    }

    /**
     * SecurityContextHolder Authentication 값 생성
     * @param token
     * @return
     */
    public Authentication getAuthentication(String token) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getMemberId(token));
        return new UsernamePasswordAuthenticationToken(userDetails.getUsername(), userDetails.getPassword(), userDetails.getAuthorities());
    }

    /**
     * JWT Claims Body의 member 값 가져오기
     * @param token
     * @return
     */
    private String getMemberId(String token) {
        return (String) Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("member");
    }

}
