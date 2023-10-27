package tech.cdnl.goword.exceptions;

public class ResourceConflictException extends RuntimeException {

    private String code;

    public String getCode() {
        return code;
    }

    public ResourceConflictException(String message) {
        super(message);
        code = AppErrorCode.RESOURCE_CONFLICT;
    }

    public ResourceConflictException(String message, String code) {
        super(message);
        this.code = code;
    }
}
