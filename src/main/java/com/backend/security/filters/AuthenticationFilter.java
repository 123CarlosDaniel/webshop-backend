package com.backend.security.filters;

import com.backend.exception.CustomExceptions.NotFoundException;
import com.backend.models.entities.Admin;
import com.backend.repository.AdminRepository;
import com.backend.security.utils.JwtUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {
    private static final List<RequestMatcher> EXCLUDED_ENDPOINTS = List.of(
            new AntPathRequestMatcher("/api/v1/products/**", "GET"),
            new AntPathRequestMatcher("/api/v1/file-manager/files/{id}"),
            new AntPathRequestMatcher("/api/v1/auth/**")
    );
    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private JwtUtils jwtUtils;
    private boolean matchesExcludedEndpoint(HttpServletRequest request) {
        return EXCLUDED_ENDPOINTS.stream().anyMatch(matcher -> matcher.matches(request));
    }

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest request,
                                    @Nonnull HttpServletResponse response,
                                    @Nonnull  FilterChain filterChain) throws ServletException, IOException, NotFoundException {

        if (matchesExcludedEndpoint(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String jwtToken = authHeader.substring(7);
        String username;
        try {
            username = jwtUtils.extractUsername(jwtToken);
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token expired");
            return;
        }
        Admin admin = null;
        try {
            admin = adminRepository.findAdminByUsername(username).orElseThrow();
        }
        catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User not found");
            return;
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(username, null, admin.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }
}
