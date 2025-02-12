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
     * Handles {@link IllegalArgumentException} specifically.
     * Indicates an invalid argument passed to a method.
     * Returns a 400 Bad Request status with the exception’s message.
     *
     * @param ex the exception indicating an invalid argument
     * @return a {@link ResponseEntity} with a 400 Bad Request status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    /**
     * Handles {@link IllegalStateException} specifically.
     * Indicates a method has been invoked at an illegal or inappropriate time.
     * Returns a 400 Bad Request status with the exception’s message.
     *
     * @param ex the exception indicating an invalid state
     * @return a {@link ResponseEntity} with a 400 Bad Request status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(IllegalStateException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
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
     * the response body. Returns a 400 Bad Request status with a user-friendly
     * error list.
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
     * Handles {@link EmailAlreadyInUseException}, which indicates an attempt to
     * register or update using an email that is already taken.
     * Returns a 409 Conflict status with the exception’s message.
     *
     * @param ex the exception indicating a duplicate email
     * @return a {@link ResponseEntity} with a 409 Conflict status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(EmailAlreadyInUseException.class)
    public ResponseEntity<ErrorResponse> handleEmailAlreadyInUseException(EmailAlreadyInUseException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value()) // 409 Conflict
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    /**
     * Handles {@link UserNotFoundException}, which indicates a lookup for a user
     * that does not exist. Returns a 404 Not Found status with the exception’s message.
     *
     * @param ex the exception indicating the user was not found
     * @return a {@link ResponseEntity} with a 404 Not Found status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFound(UserNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value()) // 404 Not found
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link TokenExpiredException}, thrown when a verification or
     * authentication token has passed its valid time window.
     * Returns a 400 Bad Request status with a descriptive message.
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
     * Handles {@link UserAlreadyVerifiedException}, thrown when a verification action
     * is requested but the user is already verified.
     * Returns a 409 Conflict status with the exception’s message.
     *
     * @param ex the exception indicating the user is already verified
     * @return a {@link ResponseEntity} with a 409 Conflict status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(UserAlreadyVerifiedException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyVerified(UserAlreadyVerifiedException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
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
     * Handles {@link TeamNotFoundException}, which indicates that a requested team
     * does not exist.
     * Returns a 404 Not Found status with the exception’s message.
     *
     * @param ex the exception indicating the team was not found
     * @return a {@link ResponseEntity} with a 404 Not Found status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(TeamNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTeamNotFoundException(TeamNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    /**
     * Handles {@link MembershipNotFoundException}, which indicates
     * a requested membership link between a user and a team does not exist.
     * Returns a 404 Not Found status with the exception’s message.
     *
     * @param ex the exception indicating the membership was not found
     * @return a {@link ResponseEntity} with a 404 Not Found status and
     *         the exception’s message
     */
    @org.springframework.web.bind.annotation.ExceptionHandler(MembershipNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMembershipNotFoundException(MembershipNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(MembershipAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleMembershipAlreadyExistsException(MembershipAlreadyExistsException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.CONFLICT.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ScheduleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleScheduleNotFoundException(ScheduleNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(ShiftNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleShiftNotFoundException(ShiftNotFoundException ex) {
        ErrorResponse error = ErrorResponse.builder()
                .status(HttpStatus.NOT_FOUND.value())
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

}
