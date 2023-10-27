package tech.cdnl.goword.auth.services;

import org.springframework.scheduling.annotation.Async;

@Async
public interface AuthServiceAsync {
    void sendEmailVerificationCode(String email);
}
