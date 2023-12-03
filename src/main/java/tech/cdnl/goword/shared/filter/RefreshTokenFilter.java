package tech.cdnl.goword.shared.filter;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.Base64;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import lombok.RequiredArgsConstructor;
import tech.cdnl.goword.auth.models.dto.SessionDto;
import tech.cdnl.goword.auth.models.entity.Session;
import tech.cdnl.goword.auth.repos.SessionRepo;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.shared.AppConstant;
import tech.cdnl.goword.user.models.dto.UserDto;
import tech.cdnl.goword.user.models.entity.User;
import tech.cdnl.goword.user.repos.UserRepo;

@RequiredArgsConstructor
@Component
public class RefreshTokenFilter extends OncePerRequestFilter {
	private final SessionRepo sessionRepo;
	private final UserRepo userRepo;
	private final Base64.Encoder base64Encoder;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
			FilterChain filterChain)
			throws ServletException, IOException {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth == null) {
			String refToken = request.getHeader("Authorization");
			if (refToken != null && refToken.startsWith("Bearer " + AppConstant.REFRESH_TOKEN_PREFIX)) {
				refToken = refToken.replace("Bearer ", "");
				refToken = base64Encoder.encodeToString(refToken.getBytes());
				Session session = sessionRepo.findByRefreshToken(refToken)
						.orElseThrow(() -> new BadCredentialsException(
								AppErrorMessage.REFRESH_TOKEN_INVALID));
				if (ZonedDateTime.now().toEpochSecond() < session.getRefreshTokenExpireAt()) {
					User user = userRepo.findById(session.getUserId())
							.orElseThrow(() -> new BadCredentialsException(
									AppErrorMessage.USER_NOT_FOUND));
					UserDto userDto = new UserDto(
							user.getId(),
							user.getFirstName(),
							user.getLastName(),
							user.getEmail());
					SessionDto sessionDto = new SessionDto(session.getId(),
							session.getSignedInAt(), session.getSignedOutAt(),
							session.getRefreshToken(), userDto);
					auth = UsernamePasswordAuthenticationToken.authenticated(sessionDto, null,
							null);
					SecurityContextHolder.getContext().setAuthentication(auth);
				} else {
					throw new BadCredentialsException(AppErrorMessage.EXPIRED_REFRESH_TOKEN);
				}
			}
		}
		filterChain.doFilter(request, response);
	}

}
