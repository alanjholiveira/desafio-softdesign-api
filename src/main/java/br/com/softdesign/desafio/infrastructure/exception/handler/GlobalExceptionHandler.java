package br.com.softdesign.desafio.infrastructure.exception.handler;

import br.com.softdesign.desafio.infrastructure.exception.*;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // -----------------------------------------------------------------------
    // 404 – Not Found
    // -----------------------------------------------------------------------

    @ExceptionHandler({ PollNotFoundException.class, SessionNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleNotFound(RuntimeException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return buildResponse(HttpStatus.NOT_FOUND, "Not Found", ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // 409 – Conflict
    // -----------------------------------------------------------------------

    @ExceptionHandler({ AssociateVoteUniqueException.class, SessionOpenException.class })
    public ResponseEntity<ErrorResponse> handleConflict(RuntimeException ex) {
        log.warn("Conflict: {}", ex.getMessage());
        return buildResponse(HttpStatus.CONFLICT, "Conflict", ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // 422 – Unprocessable Entity
    // -----------------------------------------------------------------------

    @ExceptionHandler({ VotingClosedException.class, SessionNotCountVoteException.class,
            AssociateUnableToVoteException.class, AssociateNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleUnprocessable(RuntimeException ex) {
        log.warn("Unprocessable request: {}", ex.getMessage());
        return buildResponse(HttpStatus.UNPROCESSABLE_ENTITY, "Unprocessable Entity", ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // 400 – Bad Request (Bean Validation)
    // -----------------------------------------------------------------------

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException ex) {
        String details = ex.getBindingResult().getFieldErrors().stream()
                .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Validation error: {}", details);
        return buildResponse(HttpStatus.BAD_REQUEST, "Bad Request", details);
    }

    // -----------------------------------------------------------------------
    // Feign / External service
    // -----------------------------------------------------------------------

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<ErrorResponse> handleFeignStatusException(FeignException ex) {
        HttpStatus status = HttpStatus.resolve(ex.status());
        if (status == null) {
            status = HttpStatus.BAD_GATEWAY;
        }
        log.error("External service error [{}]: {}", ex.status(), ex.getMessage());
        return buildResponse(status, "External Service Error", ex.getMessage());
    }

    // -----------------------------------------------------------------------
    // Generic fallback
    // -----------------------------------------------------------------------

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        log.error("Unexpected error: {}", ex.getMessage(), ex);
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error",
                "An unexpected error occurred. Please contact support.");
    }

    // -----------------------------------------------------------------------
    // Helper
    // -----------------------------------------------------------------------

    private ResponseEntity<ErrorResponse> buildResponse(HttpStatus status, String error, String message) {
        ErrorResponse body = ErrorResponse.builder()
                .status(status.value())
                .error(error)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        return ResponseEntity.status(status).body(body);
    }

}
