package com.campusfix.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {
    public record ErrorResponse(Instant timestamp, int status, String message, String path) {}
    private ErrorResponse error(HttpStatus status, String message, HttpServletRequest request) { return new ErrorResponse(Instant.now(), status.value(), message, request.getRequestURI()); }
    @ExceptionHandler(ResourceNotFoundException.class) ResponseEntity<ErrorResponse> notFound(ResourceNotFoundException e, HttpServletRequest r) { return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error(HttpStatus.NOT_FOUND, e.getMessage(), r)); }
    @ExceptionHandler({BadRequestException.class, IllegalArgumentException.class}) ResponseEntity<ErrorResponse> badRequest(RuntimeException e, HttpServletRequest r) { return ResponseEntity.badRequest().body(error(HttpStatus.BAD_REQUEST, e.getMessage(), r)); }
    @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ErrorResponse> validation(MethodArgumentNotValidException e, HttpServletRequest r) { String message = e.getBindingResult().getFieldErrors().stream().map(FieldError::getField).collect(Collectors.joining(", ", "Invalid fields: ", "")); return ResponseEntity.badRequest().body(error(HttpStatus.BAD_REQUEST, message, r)); }
    @ExceptionHandler(Exception.class) ResponseEntity<ErrorResponse> unknown(Exception e, HttpServletRequest r) { return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error(HttpStatus.INTERNAL_SERVER_ERROR, "Unexpected server error", r)); }
}
