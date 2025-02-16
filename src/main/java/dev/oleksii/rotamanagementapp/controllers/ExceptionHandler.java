package dev.oleksii.rotamanagementapp.controllers;

import dev.oleksii.rotamanagementapp.domain.dtos.ErrorResponse;
import dev.oleksii.rotamanagementapp.exceptions.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * GlobalExceptionHandler is responsible for catching
 * and processing exceptions thrown in the application, returning a standardized
 * JSON response to the client.
 *
 * <p>By annotating this class with {@link ControllerAdvice}, we allow Spring
 * to scan and intercept exceptions thrown from any controller method. Using
 * {@link org.springframework.web.bind.annotation.ExceptionHandler} within
 * this class, each specific exception maps to a custom response with the
 * appropriate HTTP status code.</p>
 */
@RestController
@ControllerAdvice
@Slf4j
public class ExceptionHandler {

    /**
     * Catches any unhandled {@link Exception} that isn’t caught by other
     * more specific handlers. Returns a 500 Internal Server Error status
     * with a generic error message.
     *
     * @param ex the unhandled exception
     * @return a {@link ResponseEntity} with an {@link ErrorResponse} containing
     *         a generic 500 error message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception ex) {
        log.error("Unhandled exception occurred", ex);

        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Unexpected error occurred")
                .build();

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handles Spring Security’s {@link BadCredentialsException}, thrown
     * when authentication fails due to invalid username or password.
     * Returns a 401 Unauthorized status with a custom “Incorrect username or password” message.
     *
     * @param ex the exception indicating bad credentials
     * @return a {@link ResponseEntity} with a 401 Unauthorized status and
     *         a predefined error message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponse> handleBadCredentialsException(BadCredentialsException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.UNAUTHORIZED.value())
                .message("Incorrect username or password")
                .build();
        return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
    }

    /**
     * Handles validation errors triggered by {@code @Valid} or {@code @Validated}
     * annotations on DTOs. Gathers all field error messages and returns them in
     * the response body. Returns a 400 Bad Request status with error list.
     *
     * @param ex the exception containing details about validation failures
     * @return a {@link ResponseEntity} with a 400 Bad Request status and a list
     *         of field errors
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {

        // Convert each field error into a custom FieldError object
        List<ErrorResponse.FieldError> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> new ErrorResponse.FieldError(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        log.error("Validation error(s): {}", errors);

        // Build the error response indicating validation failure
        ErrorResponse errorResponse = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message("Validation failed for one or more fields")
                .errors(errors)
                .build();

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link TokenExpiredException}, thrown when a verification or
     * authentication token has passed its valid time window.
     * Returns a 400 Bad Request status with the exception’s message.
     *
     * @param ex the exception indicating token expiration
     * @return a {@link ResponseEntity} with a 400 Bad Request status
     *         and the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponse> handleTokenExpiredException(TokenExpiredException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link AccessDeniedException}, thrown when the user lacks
     * sufficient privileges to perform an action.
     * Returns a 403 Forbidden status with the exception’s message.
     *
     * @param ex the exception indicating insufficient access rights
     * @return a {@link ResponseEntity} with a 403 Forbidden status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAccessDeniedException(AccessDeniedException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
    }

    /**
     * Handles {@link NotFoundException}, indicating a requested resource
     * (e.g., user, team, or membership) does not exist.
     * Returns a 404 Not Found status with the exception’s message.
     *
     * @param ex the exception indicating a resource could not be found
     * @return a {@link ResponseEntity} with a 404 Not Found status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(NotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link ConflictException}, indicating a resource conflict
     * (e.g., duplicate entry or already-in-use scenario).
     * Returns a 409 Conflict status with the exception’s message.
     *
     * @param ex the exception indicating a resource conflict
     * @return a {@link ResponseEntity} with a 409 Conflict status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(ConflictException.class)
    public ResponseEntity<ErrorResponse> handleConflictException(ConflictException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles {@link IllegalArgumentException} and {@link IllegalStateException},
     * both of which indicate invalid input or method state, typically warranting
     * a 400 Bad Request response.
     *
     * @param ex the exception indicating an invalid argument or state
     * @return a {@link ResponseEntity} with a 400 Bad Request status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler({
            IllegalArgumentException.class,
            IllegalStateException.class
    })
    public ResponseEntity<ErrorResponse> handleBadRequestException(RuntimeException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
