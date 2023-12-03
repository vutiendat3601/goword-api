package tech.cdnl.goword.exceptions;

import org.springframework.security.core.AuthenticationException;

public class AuthException extends AuthenticationException {

    private final String errorCode;

    public AuthException(String msg) {
        super(msg);
        errorCode = AppErrorCode.UNAUTHORIZED;
    }

    public AuthException(String msg, String errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
