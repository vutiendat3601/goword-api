package tech.cdnl.goword.shared.filter;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import tech.cdnl.goword.shared.AppConstant;
import tech.cdnl.goword.shared.utils.JwtUtil;

@RequiredArgsConstructor
@Component
public class JwtTokenFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        // ## Is unathenticated ?
        if (auth == null) {
            String jwtToken = request.getHeader("Authorization");
            // ## Is jwt token ?
            if (jwtToken != null && jwtToken.startsWith("Bearer " + AppConstant.JWT_TOKEN_PREFIX)) {
                jwtToken = jwtToken.replace("Bearer ", "");
                Claims claims = jwtUtil.decodeJwtToken(jwtToken);
                // ## Is valid jwt token ?
                if (claims != null) {
                    List<?> authorities = claims.get("acl", List.class);
                    Collection<? extends GrantedAuthority> grantedAuthorities = authorities.stream()
                            .map(authority -> new SimpleGrantedAuthority(authority + "")).toList();
                    SecurityContextHolder.getContext().setAuthentication(
                            UsernamePasswordAuthenticationToken.authenticated(null, null, grantedAuthorities));
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
