package com.lemon.mdcord.common.security.jwt;

import com.lemon.mdcord.common.exception.UserAuthenticationException;
import com.lemon.mdcord.service.member.JpaMemberDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class JwtProvider {

    private final String header;
    private final Key key;
    private final long validitySeconds;
    private final String domain;
    private final boolean isProduct;
    private final UserDetailsService userDetailsService;
    private final SignatureAlgorithm signatureAlgorithm;

    public JwtProvider(
            @Value("${jwt.secret}") String secret
            , @Value("${jwt.header}") String header
            , @Value("${jwt.domain}") String domain
            , @Value("${spring.profiles.active}") String profilesActive
            , @Value("${jwt.validity-in-seconds}") long validitySeconds
            , JpaMemberDetailsService memberDetailsService) {
        this.header = header;
        this.key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.validitySeconds = validitySeconds;
        this.domain = domain;
        this.isProduct = profilesActive.equals("prod") ? true : false;
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
        long validityMiliSeconds = validitySeconds * 1000;
        Date expiration = new Date(now.getTime() + validityMiliSeconds);

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
     * Cookie에 정해진 header값이 있다면 값 리턴
     * @param request
     * @return
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

        // TODO - 처음 로그인할 때도 이 log가 찍힐 것이라서 만료가 된 것인지 구분이 불가능함. 방법 있을지 고민해보기
//        log.error("Cookies don't have headers set");
        return null;
    }

    /**
     * 토큰 검증
     * resolveToken()에서 얻은 토큰을 검증
     * @param token
     * @return
     */
    public boolean validateToken(String token, HttpServletResponse response) {
        try {
            Claims claims = getClaims(token);
            if(updateToken(claims, response)) return true;

            return validateExpiration(claims);
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
     * token claims 구하기
     * @param token
     * @return
     */
    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * 토큰 검증 시, 만료되지 않았으면서 유효 시간의 1/3 이하면 갱신
     * @param claims
     */
    private boolean updateToken(Claims claims, HttpServletResponse response) {
        LocalDateTime expiration = claims.getExpiration().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();

        long seconds = Duration.between(now, expiration).getSeconds();

        if(validateExpiration(claims) && seconds <= validitySeconds/3) {
            String token = createToken((String) claims.get("member"), (String) claims.get("role"));
            createTokenInCookie(token, response);
            return true;
        }

        return false;
    }

    /**
     * 토큰의 만료 시간과 현재 시간을 비교
     * 현재 시간이 만료 시간을 넘겼다면 false
     * @param claims
     * @return
     */
    private boolean validateExpiration(Claims claims) {
        return !claims.getExpiration().before(new Date());
    }

    /**
     * 토큰을 쿠키에 저장
     * @param token
     * @param response
     */
    public void createTokenInCookie(String token, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from(header, token)
                .maxAge(validitySeconds)
                .path("/")
                .domain(domain)
                .secure(isProduct)
                .httpOnly(true)
                .sameSite("Lax")
                .build();

        response.setHeader("Set-Cookie", cookie.toString());
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
