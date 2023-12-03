package tech.cdnl.goword.exceptions;

public class AppErrorCode {
    public static final String RESOURCE_NOT_FOUND = "E404";
    public static final String RESOURCE_CONFLICT = "E409";
    public static final String RESOURCE_CONFLICT_EMAIL_DUPLICATED = "E40901";
    public static final String RESOURCE_CONFLICT_PLAN_DUPLICATED = "E40902";

    public static final String SERVER_ERROR = "E500";
    public static final String UNAUTHORIZED = "E401";
    public static final String ACCESS_FORBIDDEN = "E403";

    public static final String VERIFICATION_CODE_INVALID = "E40001";
    public static final String ACCOUNT_EMAIL_NOT_VERIFIED = "E4011";
}
