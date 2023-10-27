package tech.cdnl.goword.exceptions;

public class ResourceNotFoundException extends RuntimeException {
    private String code;

    public String getCode() {
        return code;
    }

    public ResourceNotFoundException(String message) {
        super(message);
        code = AppErrorCode.RESOURCE_NOT_FOUND;
    }

    public ResourceNotFoundException(String message, String code) {
        super(message);
        this.code = code;
    }
}
