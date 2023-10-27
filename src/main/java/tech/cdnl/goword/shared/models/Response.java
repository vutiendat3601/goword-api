package tech.cdnl.goword.shared.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class Response<T> {
    T data;

    private ApiResponseStatus status;

    public Response(ApiResponseStatus status) {
        this.status = status;
    }

    public Response(ApiResponseStatus status, T data) {
        this.status = status;
        this.data = data;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status.toString().toLowerCase();
    }
}
