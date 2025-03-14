package com.example.booking.interceptor.response;

import com.example.booking.interceptor.Violation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GlobalResponse {

    private String message;
    private Object responseData;
    private boolean success;
    private int status;
    private Violation violations;
    private String path;
    private Long timestamp = System.currentTimeMillis();

    // Success response
    public GlobalResponse(String message, Object data, int status, String path) {
        this.message = message;
        this.responseData = data;
        this.success = true;
        this.status = status;
        this.violations = null;
        this.path = path;
    }

    // Error response
    public GlobalResponse(String message, int status, Violation violations, String path) {
        this.message = message;
        this.success = false;
        this.status = status;
        this.violations = violations;
        this.path = path;
        this.responseData = null;
    }

}

