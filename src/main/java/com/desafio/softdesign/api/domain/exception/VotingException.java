package com.desafio.softdesign.api.domain.exception;

/**
 * Exceção de negócio específica para falhas no processo de votação.
 * Permite que o Controller capture e retorne um erro de forma controlada.
 */
public class VotingException extends RuntimeException {
    private final String errorCode;

    public VotingException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public VotingException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}
