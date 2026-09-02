package org.example.opsflow.security.filter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.opsflow.common.exception.BusinessException;
import org.example.opsflow.security.session.LoginSessionService;
import org.springframework.lang.NonNull;
import lombok.RequiredArgsConstructor;
import org.example.opsflow.rbac.dto.PermissionResponse;
import org.example.opsflow.rbac.service.RolePermissionService;
import org.example.opsflow.security.handler.JwtAuthenticationEntryPoint;
import org.example.opsflow.security.jwt.JwtUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final JwtAuthenticationEntryPoint authenticationEntryPoint;
    private final RolePermissionService rolePermissionService;
    private final LoginSessionService loginSessionService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // 读取请求头
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        // 只有头中包含"Bearer token"放行，不包含就进入if里面的过滤链
        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }
        try {
            String authToken = authHeader.substring(7);
            Claims claims = jwtUtil.parseToken(authToken);
            String username = claims.getSubject();
            if(!loginSessionService.isValid(username,authToken)){
                throw  new BadCredentialsException("登录状态已失效");
            }

            List<SimpleGrantedAuthority> authorities = rolePermissionService
                    .getCurrentUserPermissions(username)
                    .stream()
                    .map(PermissionResponse::getCode)
                    .map(SimpleGrantedAuthority::new)
                    .toList();
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(username, null, authorities);
            SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
            securityContext.setAuthentication(authentication);
            SecurityContextHolder.setContext(securityContext);
        }catch (JwtException | IllegalArgumentException | BusinessException | BadCredentialsException e){
            SecurityContextHolder.clearContext();
            BadCredentialsException authenticationException = new BadCredentialsException("认证失败",e);
            authenticationEntryPoint.commence(request,response,authenticationException);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
