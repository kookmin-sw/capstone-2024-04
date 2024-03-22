package com.drm.server.config.jwt;


import com.drm.server.common.ErrorResponse;
import com.drm.server.common.enums.ErrorCode;
import com.drm.server.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_TYPE = "Bearer";

    private final RedisTemplate redisTemplate;
    private final JwtTokenProvider jwtTokenProvider;

    private  final CustomUserDetailsService customUserDetailsService;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        // Request Header에서 JWT 토큰 추출
        String token = resolveToken( request);

        try {
            // validateToken으로 토큰 유효성 검사
            if (token != null) {
                if(jwtTokenProvider.validateToken(token)) {
                    // 토큰이 유효할 경우 토큰에서 Authentication 객체를 가져와서 SecurityContext에 저장
                    Authentication authentication = jwtTokenProvider.getAuthentication(token);
                    String refreshToken = (String)redisTemplate.opsForValue().get("RT:" + authentication.getName());
                    log.warn(refreshToken);
                    if( !(refreshToken == null || refreshToken.isBlank())){
                        SecurityContextHolder.getContext().setAuthentication(authentication);
                    }else {
                        handleUnauthorizedException("로그아웃한 유저",response);;
                        return;
                    }
                }
            }
        } catch (SecurityException | MalformedJwtException e) {
            // 401 Unauthorized
            handleUnauthorizedException("Invalid JWT Token", response);
            return;
        } catch (ExpiredJwtException e) {
            // 401 Forbidden
            handleUnauthorizedException("Expired JWT Token",response);
            return;
        } catch (UnsupportedJwtException e) {
            // 400 Bad Request
            handleBadRequestException("Unsupported JWT Token", response);
            return;
        } catch (IllegalArgumentException e) {
            // 400 Bad Request
            handleBadRequestException("JWT claims string is empty",response);
            return;
        } catch (SignatureException e){
            handleUnauthorizedException("Invalid JWT Token", response);
            return;
        }catch(io.jsonwebtoken.io.DecodingException e){
            handleUnauthorizedException("Invalid JWT Token",response);
            return;

        }catch (Exception e) {
            // 예외 처리
            handleException(e,  response);
            return;
        }

        // 예외가 발생하지 않은 경우에는 계속 다음 필터 또는 서블릿으로 요청을 전달
        chain.doFilter(request, response);
    }

    private void handleUnauthorizedException(String message,HttpServletResponse response) throws IOException {
        // 401 Unauthorized
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED_ERROR,message);
        writeErrorResponse(response, errorResponse);
    }

    private void handleForbiddenException(HttpServletResponse response) throws IOException {
        // 403 Forbidden
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.UNAUTHORIZED_ERROR,"권한 거부");
        writeErrorResponse(response, errorResponse);;
    }

    private void handleBadRequestException(String message,HttpServletResponse response) throws IOException {
        // 400 Bad Request
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_PARAMETER,message);
        writeErrorResponse(response, errorResponse);;
    }

    private void handleTokenValidationException(HttpServletResponse response) throws IOException {
        // validateToken 메서드에서 발생한 예외에 따라 적절한 응답 생성
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_PARAMETER,"Token is empty");
        writeErrorResponse(response, errorResponse);
    }

    private void handleException(Exception e, HttpServletResponse response) throws IOException {
        // 예외에 따라 적절한 응답 생성
        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR,e.getMessage());
        writeErrorResponse(response, errorResponse);
    }
    private void writeErrorResponse(HttpServletResponse response, ErrorResponse errorResponse) throws IOException {
        // ErrorResponse를 JSON 형태로 변환하여 클라이언트에 응답 전송
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }

    // Request Header 에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_TYPE)) {
            return bearerToken.substring(7);
        }
        return null;
    }
}