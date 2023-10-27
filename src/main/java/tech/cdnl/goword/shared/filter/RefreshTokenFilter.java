package tech.cdnl.goword.shared.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import tech.cdnl.goword.shared.AppConstant;

@Component
public class RefreshTokenFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            String refToken = request.getHeader("Authorization");
            if (refToken != null && refToken.startsWith("Bearer " + AppConstant.REFRESH_TOKEN_PREFIX)) {
                refToken = refToken.replace("Bearer ", "");
                SecurityContextHolder.getContext()
                        .setAuthentication(UsernamePasswordAuthenticationToken.unauthenticated(null, refToken));
            }
        }
        filterChain.doFilter(request, response);
    }

}
