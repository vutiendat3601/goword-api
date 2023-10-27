package tech.cdnl.goword.shared.models;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class PageResponse<T> {

    List<T> data;

    int page;

    int totalPages;

    int size;

    int numOfElements;
    private ApiResponseStatus status;

    public PageResponse(ApiResponseStatus status, List<T> data, int page, int totalPages, int size, int numOfElements) {
        this.data = data;
        this.page = page;
        this.totalPages = totalPages;
        this.size = size;
        this.numOfElements = numOfElements;
        this.status = status;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status.toString().toLowerCase();
    }
}
