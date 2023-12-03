package tech.cdnl.goword.auth.services.impl;

import java.time.ZonedDateTime;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import tech.cdnl.goword.auth.models.dto.SessionDto;
import tech.cdnl.goword.auth.models.entity.Session;
import tech.cdnl.goword.auth.repos.SessionRepo;
import tech.cdnl.goword.auth.services.AuthServiceAsync;
import tech.cdnl.goword.exceptions.AppErrorCode;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.AuthException;
import tech.cdnl.goword.user.models.dto.UserDto;
import tech.cdnl.goword.user.models.entity.User;
import tech.cdnl.goword.user.repos.UserRepo;

@Service
@RequiredArgsConstructor
public class UsernamePasswordProvider implements AuthenticationProvider {
    private final UserRepo userRepo;
    private final PasswordEncoder passEncoder;
    private final SessionRepo sessionRepo;
    private final AuthServiceAsync authServiceAsync;

    @Override
    public Authentication authenticate(Authentication auth) throws AuthenticationException {
        String email = auth.getPrincipal() + "";
        String password = auth.getCredentials() + "";
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new AuthException(AppErrorMessage.USER_NOT_FOUND));
        if (!user.isVerified()) {
            if (user.getVerificationCodeExpireAt() == null ||
                    user.getVerificationCodeExpireAt() <= ZonedDateTime.now().toEpochSecond()) {
                authServiceAsync.sendEmailVerificationCode(user.getEmail());
            }
            throw new AuthException(AppErrorMessage.ACCOUNT_EMAIL_NOT_VERIFIED,
                    AppErrorCode.ACCOUNT_EMAIL_NOT_VERIFIED);
        }
        if (passEncoder.matches(password, user.getPassword())) {
            Session session = new Session(user.getId(), ZonedDateTime.now());
            session.setCreatedBy(email);
            session.setUpdatedBy(email);
            session = sessionRepo.save(session);
            UserDto userDto = new UserDto(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
            SessionDto sessionDto = new SessionDto(
                    session.getId(),
                    session.getSignedInAt(),
                    session.getSignedOutAt(),
                    session.getRefreshToken(),
                    userDto);

            return UsernamePasswordAuthenticationToken.authenticated(sessionDto, null, null);
        }
        throw new BadCredentialsException(AppErrorMessage.WRONG_PASSWORD);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.isAssignableFrom(UsernamePasswordAuthenticationToken.class);
    }

}
