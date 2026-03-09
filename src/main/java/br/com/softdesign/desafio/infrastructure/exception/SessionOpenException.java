package br.com.softdesign.desafio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class SessionOpenException extends RuntimeException {

    public SessionOpenException() {
        super("Poll is now open for voting");
    }

}
