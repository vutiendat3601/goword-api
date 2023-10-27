package tech.cdnl.goword.auth.services;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.validation.annotation.Validated;

import tech.cdnl.goword.auth.models.dto.SessionDto;
import tech.cdnl.goword.auth.models.dto.TokenDto;
import tech.cdnl.goword.auth.models.request.SignUpRequest;
import tech.cdnl.goword.user.models.dto.UserDto;

public interface AuthService {
    void signUp(SignUpRequest signUpReq);

    @Validated
    TokenDto generateToken(SessionDto sessionDto);

    void verifyEmail(String code);

    Collection<GrantedAuthority> getAllAuthorities(UserDto userDto);

    void signOut(UUID sessionId);
}
