package tech.cdnl.goword.exceptions;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import tech.cdnl.goword.shared.models.ApiResponseStatus;

@Data
public class ErrorResponse {
    private ApiResponseStatus status;

    private String message;

    private String code;

    private long timestamp;

    public ErrorResponse(ApiResponseStatus status, String message, String code, long timestamp) {
        this.status = status;
        this.message = message;
        this.code = code;
        this.timestamp = timestamp;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status.toString().toLowerCase();
    }

}
