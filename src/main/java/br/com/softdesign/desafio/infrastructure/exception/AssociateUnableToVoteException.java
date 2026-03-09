package br.com.softdesign.desafio.infrastructure.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)

public class AssociateUnableToVoteException extends RuntimeException {

    public AssociateUnableToVoteException() {
        super("Your TaxID is not authorized for this vote.");
    }

}
