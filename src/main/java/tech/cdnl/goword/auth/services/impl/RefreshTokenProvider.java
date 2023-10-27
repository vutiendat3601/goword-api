package tech.cdnl.goword.auth.services.impl;

import java.time.ZonedDateTime;
import java.util.Base64;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import tech.cdnl.goword.auth.models.dto.SessionDto;
import tech.cdnl.goword.auth.models.entity.Session;
import tech.cdnl.goword.auth.repos.SessionRepo;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.user.models.dto.UserDto;
import tech.cdnl.goword.user.models.entity.User;
import tech.cdnl.goword.user.repos.UserRepo;

@AllArgsConstructor
@Service
public class RefreshTokenProvider implements AuthenticationProvider {
    private final SessionRepo sessionRepo;
    private final UserRepo userRepo;
    private final Base64.Encoder base64Encoder;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String refToken = auth.getCredentials().toString();
        refToken = base64Encoder.encodeToString(refToken.getBytes());
        Session session = sessionRepo.findByRefreshToken(refToken)
                .orElseThrow(() -> new BadCredentialsException(AppErrorMessage.REFRESH_TOKEN_INVALID));
        if (ZonedDateTime.now().toEpochSecond() < session.getRefreshTokenExpireAt()) {
            User user = userRepo.findById(session.getUserId())
                    .orElseThrow(() -> new BadCredentialsException(AppErrorMessage.USER_NOT_FOUND));
            UserDto userDto = new UserDto(
                    user.getId(),
                    user.getFirstName(),
                    user.getLastName(),
                    user.getEmail());
            SessionDto sessionDto = new SessionDto(session.getId(),
                    session.getSignedInAt(), session.getSignedOutAt(), session.getRefreshToken(), userDto);

            return UsernamePasswordAuthenticationToken.authenticated(sessionDto, null, null);
        }
        throw new BadCredentialsException(AppErrorMessage.EXPIRED_REFRESH_TOKEN);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

}
