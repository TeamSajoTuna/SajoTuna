package com.sajoproject.sajotuna.config;

import com.sajoproject.sajotuna.enums.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.servlet.*;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

@Slf4j
@RequiredArgsConstructor
// HTTP 요청에서 JWT 검증, 요청이 허용된 경로에 대해서만 접근 허용
public class JwtFilter implements Filter {

    private final JwtUtil jwtUtil;
    private final Pattern authPattern = Pattern.compile("^/users.*");

    // 필터 초기화 메서드
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    // HTTP 요청에서 JWT 토큰 추출 및 검증 메서드
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        String url = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();


        if (url.startsWith("/users/signup") || url.startsWith("/users/signin")) {
            chain.doFilter(request, response);
            return;
        }

        // Authorization 헤더에서 JWT 토큰 추출
        String bearerJwt = httpRequest.getHeader("Authorization");

        // JWT 토큰 유효성 검증
        if (bearerJwt == null || !bearerJwt.startsWith("Bearer ")) {
            log.error("Authorization header is missing or not in Bearer format.");
            // 토큰이 없는 경우 400을 반환
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "JWT 토큰이 필요합니다.");
            return;
        }

        String jwt = jwtUtil.substringToken(bearerJwt);

        try {
            // JWT 유효성 검사와 claims 추출
            Claims claims = jwtUtil.extractClaims(jwt);

            // setAttribute
            request.setAttribute("userId", Long.parseLong(claims.get("sub", String.class)));
            request.setAttribute("email", claims.get("email",String.class));
            request.setAttribute("userRole",claims.get("userRole",String.class));

            // 관리자 권한이 필요한 경로 설정
            List<String> allowedMethods = Arrays.asList("PUT", "DELETE");
            String pathPrefix = "/users/";

            // 경로와 메서드 체크 및 관리자 권한이 필요한 경로인지 확인 -> 권한 X 일 경우 403 리턴
            if(checkMethodPath(method, url, allowedMethods, pathPrefix)) {
                String userRole = claims.get("userRole", String.class);
                if (!UserRole.ADMIN.name().equals(userRole)){
                    httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN,"You do not have permission to access this resource.");
                    return;
                }
            }

            chain.doFilter(request, response);
        } catch (SecurityException | MalformedJwtException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "유효하지 않는 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 JWT 토큰입니다.");
        } catch (Exception e) {
            log.error("JWT 토큰 검증 중 오류가 발생했습니다.", e);
            httpResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "JWT 토큰 검증 중 오류가 발생했습니다.");
        }
    }

    // 필터 종료 시 호출 되는 메서드
    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    private boolean checkMethodPath(String method, String url, List<String> allowedMethods, String pathPrefix) {
        // HTTP 메서드와 URL 경로 검사
        if (!checkHttpMethod(method, allowedMethods)) {
            return false;
        }
        // 요청 URL 이 특정 경로로 시작하는지 검사
        if (!checkPathUrl(url, pathPrefix)) {
            return false;
        }

        String idPart = extractIdFromPath(url, pathPrefix);
        return isNumeric(idPart);
    }

    // 요청 메서드가 관리자 권한이 필요한 메서드 목록에 포함되는지 확인
    private boolean checkHttpMethod(String method, List<String> allowedMethods) {
        return allowedMethods.stream().anyMatch(allowedMethod -> allowedMethod.equalsIgnoreCase(method));
    }

    // 요청 URL이 특정 경로로 시작하는지 확인
    private boolean checkPathUrl(String url, String pathPrefix) {
        return url.startsWith(pathPrefix);
    }

    // 경로에서 ID 부분 추출 ex) '/board/123' 에서 '123' 추출
    private String extractIdFromPath(String url, String pathPrefix) {
        return url.substring(pathPrefix.length());
    }

    // 추출한 id가 숫자인지 확인
    private boolean isNumeric(String str) {
        return str != null && str.matches("\\d+");
    }
}