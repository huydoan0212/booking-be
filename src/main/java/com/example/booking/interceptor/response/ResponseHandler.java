package com.example.booking.interceptor.response;

import jakarta.annotation.Nonnull;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class ResponseHandler implements ResponseBodyAdvice<Object> {

    @SuppressWarnings("null")
    @Override
    public boolean supports(
            @Nonnull MethodParameter returnType,
            @Nonnull Class<? extends HttpMessageConverter<?>> converterType
    ) {
        return MappingJackson2HttpMessageConverter.class.isAssignableFrom(converterType);
    }

    @SuppressWarnings("null")
    @Override
    public Object beforeBodyWrite(
            Object body,
            @Nonnull MethodParameter returnType,
            @Nonnull MediaType selectedContentType,
            @Nonnull Class<? extends HttpMessageConverter<?>> selectedConverterType,
            ServerHttpRequest request,
            @Nonnull ServerHttpResponse response
    ) {

        // ignore intercepting if url contains /swagger, /api-docs
        if (request.getURI().getPath().contains("/swagger") || request.getURI().getPath().contains("/api-docs")) {
            return body;
        }

        // ignore intercepting if body is already a GlobalResponse
        if (body instanceof GlobalResponse) {
            return body;
        }

        // Prepare response status
        final HttpStatus status;
        if (request.getMethod().toString().equals("POST")) {
            status = HttpStatus.CREATED;
        } else {
            status = HttpStatus.OK;
        }

        // Wrap response in GlobalResponse
        return new GlobalResponse(
                "Success",
                body,
                status.value(),
                request.getURI().getPath()
        );
    }

}

