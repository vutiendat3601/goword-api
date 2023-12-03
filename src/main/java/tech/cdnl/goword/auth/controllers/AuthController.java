package tech.cdnl.goword.auth.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import tech.cdnl.goword.auth.models.dto.SessionDto;
import tech.cdnl.goword.auth.models.dto.TokenDto;
import tech.cdnl.goword.auth.models.request.UserRequest;
import tech.cdnl.goword.auth.services.AuthService;
import tech.cdnl.goword.exceptions.AppErrorMessage;
import tech.cdnl.goword.exceptions.AuthException;
import tech.cdnl.goword.shared.models.ApiResponseStatus;
import tech.cdnl.goword.shared.models.Response;

@Validated
@RequiredArgsConstructor
@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("sign-up")
    public ResponseEntity<Response<?>> signUp(@Validated(UserRequest.SignUp.class) @RequestBody UserRequest userReq) {
        authService.signUp(userReq);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS));
    }

    @PostMapping("token")
    public ResponseEntity<Response<TokenDto>> generateToken() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();
        if (!(principal instanceof SessionDto)) {
            throw new AuthException(AppErrorMessage.UNAUTHORIZED);
        }
        TokenDto tokenDto = authService.generateToken((SessionDto) principal);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS, tokenDto));
    }

    @GetMapping("sign-out")
    public ResponseEntity<Response<?>> signOut(@RequestParam UUID sessionId) {
        authService.signOut(sessionId);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS, null));
    }

    @PostMapping("email-verification")
    public ResponseEntity<Response<?>> verifyEmail(@RequestParam String code) {
        authService.verifyEmail(code);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS));
    }

    @PutMapping
    public ResponseEntity<Response<?>> updateUser(
            @Validated(UserRequest.Update.class) @RequestBody UserRequest userReq) {
        authService.updateUser(userReq);
        return ResponseEntity.ok(new Response<>(ApiResponseStatus.SUCCESS));
    }
}