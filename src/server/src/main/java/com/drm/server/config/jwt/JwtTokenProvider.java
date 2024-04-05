package com.drm.server.config.jwt;

import com.drm.server.common.enums.Authority;
import com.drm.server.controller.dto.response.UserResponse;
import com.drm.server.domain.user.CustomUserInfoDto;
import com.drm.server.domain.user.UserRepository;
import com.drm.server.exception.TokenException;
import com.drm.server.service.CustomUserDetailsService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@AllArgsConstructor
public class JwtTokenProvider {
    private static final String AUTHORITIES_KEY = "auth";
    private static final String USER_ID = "userId";
    private static final String AUTHORITY = "authority";
    private static final String BEARER_TYPE = "Bearer";
    private static final long ACCESS_TOKEN_EXPIRE_TIME = 12 * 60 * 60 * 1000L;
    private static final long REFRESH_TOKEN_EXPIRE_TIME = 7 * 24 * 60 * 60 * 1000L;    // 7일
    @Autowired
    private CustomUserDetailsService customUserDetailsService;
    private final Key key;
    @Autowired
    private UserRepository userRepository;

    public JwtTokenProvider(){
        byte[] keyBytes = Decoders.BASE64.decode("VlwEyVBsYt9V7zq57TejMnVUyzblYcfPQye08f7MGVA9XkHN");
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public UserResponse.TokenInfo generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        // Access Token 생성
        Date accessTokenExpiresIn = new Date(now + ACCESS_TOKEN_EXPIRE_TIME);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // Refresh Token 생성
        String refreshToken = Jwts.builder()
                .setExpiration(new Date(now + REFRESH_TOKEN_EXPIRE_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        return UserResponse.TokenInfo.builder()
                .grantType(BEARER_TYPE)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                .build();
    }
    public UserResponse.TokenInfo generateToken(CustomUserInfoDto user) {
        Claims claims = Jwts.claims();
        claims.put(USER_ID, user.getUserId());
        claims.put(AUTHORITY, user.getAuthority());
//        log.info(claims.get("userId",Long.class).toString());
        if (user.getUserId() == 1 || user.getAuthority().equals(Authority.ADMIN)) {
            // Set the access token expiration time to infinity for TEST users
            Date accessTokenExpiresIn = new Date(Long.MAX_VALUE);

            // Generate the access token
            String accessToken = Jwts.builder()
                    .setClaims(claims)
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            // Generate the refresh token
            String refreshToken = Jwts.builder()
                    .setExpiration(accessTokenExpiresIn)
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return UserResponse.TokenInfo.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                    .build();
        } else {
//            // Generate the access and refresh tokens normally for non-TEST users
            ZonedDateTime now = ZonedDateTime.now();
            ZonedDateTime accessTokenExpiresIn = now.plusSeconds( ACCESS_TOKEN_EXPIRE_TIME);
            ZonedDateTime refreshTokenExpiresIn = now.plusSeconds( REFRESH_TOKEN_EXPIRE_TIME);

            // Generate the access token
            String accessToken = Jwts.builder()
                    .setClaims(claims)
                    .setIssuedAt(Date.from(now.toInstant()))
                    .setExpiration(Date.from(accessTokenExpiresIn.toInstant()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            // Generate the refresh token
            String refreshToken = Jwts.builder()
                    .setExpiration(Date.from(refreshTokenExpiresIn.toInstant()))
                    .signWith(key, SignatureAlgorithm.HS256)
                    .compact();

            return UserResponse.TokenInfo.builder()
                    .grantType(BEARER_TYPE)
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .refreshTokenExpirationTime(REFRESH_TOKEN_EXPIRE_TIME)
                    .build();
        }
    }

    // JWT 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메서드
    public Authentication getAuthentication(String accessToken) {
        // 토큰 복호화
        Claims claims = parseClaims(accessToken);

        UserDetails userDetails = customUserDetailsService.loadUserByUsername(claims.get(USER_ID,Long.class).toString());
//        return new UsernamePasswordAuthenticationToken(principal, "", authorities);
        return new UsernamePasswordAuthenticationToken(userDetails, accessToken, userDetails.getAuthorities());
    }


    // 토큰 정보를 검증하는 메서드
    public boolean validateToken(String token) throws SecurityException, ExpiredJwtException, UnsupportedJwtException, MalformedJwtException, IllegalArgumentException {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            log.info("Invalid JWT Token", e);
            // 401 Unauthorized
            throw e;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            // 401 Expired
            throw e;
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            // 400 Bad Request
            throw e;
        } catch (IllegalArgumentException e) {
            log.info("JWT claims string is empty.", e);
            // 400 Bad Request
            throw e;
        }
    }

    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }catch (SignatureException e){
            throw new TokenException("Invalid JWT Token");
        }
    }

    public Long getExpiration(String accessToken) {
        // accessToken 남은 유효시간
        Date expiration = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(accessToken).getBody().getExpiration();
        // 현재 시간
        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

}


