package br.com.softdesign.desafio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SessionNotCountVoteException extends RuntimeException {

    public SessionNotCountVoteException() {
        super("It is not possible to obtain an open poll result. Wait for closure to get the result.");
    }

}
