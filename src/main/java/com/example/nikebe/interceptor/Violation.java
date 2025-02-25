package com.example.nikebe.interceptor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class Violation {
    private String fieldName;
    private String message;
}

