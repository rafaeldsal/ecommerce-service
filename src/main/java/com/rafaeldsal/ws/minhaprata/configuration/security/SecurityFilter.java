package com.rafaeldsal.ws.minhaprata.configuration.security;

import com.rafaeldsal.ws.minhaprata.exception.UnauthorizedException;
import com.rafaeldsal.ws.minhaprata.repository.jpa.UserDetailsRepository;
import com.rafaeldsal.ws.minhaprata.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

  private final TokenService tokenService;
  private final UserDetailsRepository userDetailsRepository;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    var token = getBearerToken(request);
    if (Objects.nonNull(token) && tokenService.isValid(token)) {
      authByToken(token);
    }
    filterChain.doFilter(request, response);
  }

  private void authByToken(String token) {
    String userId = tokenService.getUserId(token);
    var userOpt = userDetailsRepository.findById(userId)
        .orElseThrow(() -> new UnauthorizedException("Token não corresponde ao usuário"));

    UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(userOpt, null, userOpt.getAuthorities());
    SecurityContextHolder.getContext().setAuthentication(userAuth);
  }

  private String getBearerToken(HttpServletRequest request) {
    String authHeader = request.getHeader("Authorization");
    if(Objects.isNull(authHeader) || !authHeader.startsWith("Bearer ")) return null;
    return authHeader.replace("Bearer ", "");
  }
}
