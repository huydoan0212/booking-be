package com.example.booking.interceptor.exception;

import com.example.booking.common.constant.ExceptionMessage;
import com.example.booking.interceptor.Violation;
import com.example.booking.interceptor.response.GlobalResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.UnexpectedTypeException;
import jakarta.validation.ValidationException;
import org.apache.coyote.BadRequestException;
import org.hibernate.TransactionException;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.orm.jpa.JpaObjectRetrievalFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketException;

@ControllerAdvice
public class _ExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(1)
    @ResponseBody
    public GlobalResponse handleException(Exception e, HttpServletRequest request) {
        return new GlobalResponse(
                e.getMessage() == null ? ExceptionMessage.INTERNAL_SERVER_ERROR : e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                null,
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(1)
    @ResponseBody
    public GlobalResponse handleBadRequestException(Exception e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation((e.getCause() != null
                        ? e.getCause().getMessage()
                        : null), e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(HttpClientErrorException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleBadRequestException(HttpClientErrorException e, HttpServletRequest request) {
        String serverMessage = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode responseBody = objectMapper.readTree(e.getResponseBodyAsString());
            serverMessage = responseBody.path("violations").path("stackTrace").asText(); // Lấy thông báo từ violations.message
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return new GlobalResponse(
                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(null, serverMessage != null ? serverMessage : e.getMessage()), // Ưu tiên sử dụng message từ server
                request.getRequestURI()
        );
    }

    @ExceptionHandler(TransactionException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(1)
    @ResponseBody
    public GlobalResponse handleTransactionException(TransactionException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return new GlobalResponse(
                e.getMessage() == null ? ExceptionMessage.INTERNAL_SERVER_ERROR : e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Violation(e.getCause().getCause().getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(1)
    @ResponseBody
    public GlobalResponse handleUnexpectedType(UnexpectedTypeException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return new GlobalResponse(
                e.getMessage() == null ? ExceptionMessage.BAD_REQUEST : e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Violation(null, e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(NullPointerException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @Order(1)
    @ResponseBody
    public GlobalResponse handleNullPointerException(NullPointerException e, HttpServletRequest request) {
        System.out.println(e.getMessage());
        return new GlobalResponse(
                e.getMessage() == null ? ExceptionMessage.BAD_REQUEST : e.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                new Violation(null, e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(InvalidDataAccessApiUsageException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @Order(1)
    @ResponseBody
    public GlobalResponse handleInvalidDataAccess(InvalidDataAccessApiUsageException e, HttpServletRequest request) {
        System.out.println(e);
        return new GlobalResponse(
                e.getMessage() == null ? ExceptionMessage.INTERNAL_SERVER_ERROR : e.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                new Violation(null, e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public GlobalResponse handleBadCredential(BadCredentialsException e, HttpServletRequest request) {
        return new GlobalResponse(ExceptionMessage.BAD_CREDENTIAL, HttpStatus.UNAUTHORIZED.value(), new Violation(e.getCause().getMessage(), e.getMessage()), request.getRequestURI());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class, ValidationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        return new GlobalResponse(

                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(e.getBindingResult().getFieldError() == null ? ExceptionMessage.BAD_REQUEST : e.getBindingResult().getFieldError().getField(),
                        e.getBindingResult().getFieldError() == null ? ExceptionMessage.BAD_REQUEST : e.getBindingResult().getFieldError().getDefaultMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public GlobalResponse handleNotFoundException(EntityNotFoundException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                new Violation(e.getCause() == null ? "" : e.getCause().getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleConstraintViolationException(DataIntegrityViolationException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(null, e.getMostSpecificCause().getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({HttpMessageConversionException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleMessageConversionException(HttpMessageConversionException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(null, e.getMostSpecificCause().getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(JpaObjectRetrievalFailureException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleDataNotUnique(JpaObjectRetrievalFailureException e, HttpServletRequest request) {
        return new GlobalResponse(ExceptionMessage.BAD_REQUEST, HttpStatus.BAD_REQUEST.value(),
                new Violation(null, e.getMostSpecificCause().getMessage()), request.getRequestURI());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleIllegalsException(IllegalArgumentException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(null, e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public GlobalResponse handleNotFound(UsernameNotFoundException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.NOT_FOUND,
                HttpStatus.NOT_FOUND.value(),
                new Violation(e.getCause().getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({AuthorizationDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public GlobalResponse handleAuthorizationDeniedException(AuthorizationDeniedException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.FORBIDDEN,
                HttpStatus.FORBIDDEN.value(),
                new Violation(e.getCause().getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({StringIndexOutOfBoundsException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleStringIndexOutOfBoundsException(StringIndexOutOfBoundsException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.BAD_REQUEST,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(e.getCause().getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }

    @ExceptionHandler({SocketException.class})
    @ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
    @ResponseBody
    public GlobalResponse handleSocketException(SocketException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.SERVER_UNAVAILABLE,
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                new Violation(e.getCause().getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }


    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public GlobalResponse handleFileNotFoundException(FileNotFoundException e, HttpServletRequest request) {
        return new GlobalResponse(
                ExceptionMessage.FILE_NOT_SUPPORTED,
                HttpStatus.BAD_REQUEST.value(),
                new Violation(e.getMessage(), e.getMessage()),
                request.getRequestURI()
        );
    }


}
