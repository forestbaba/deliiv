package com.deliiv.server.payload.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ApiResponse {
    private Boolean success;
    private String message;
    private Object payload;

    public ApiResponse(Boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
