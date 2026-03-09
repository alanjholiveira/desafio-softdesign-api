package br.com.softdesign.desafio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class VotingClosedException extends RuntimeException {

    public VotingClosedException() {
        super("Voting closed");
    }

}
