package br.com.softdesign.desafio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class AssociateNotFoundException extends RuntimeException {

    public AssociateNotFoundException() {
        super("Associate Not Found");
    }
}
