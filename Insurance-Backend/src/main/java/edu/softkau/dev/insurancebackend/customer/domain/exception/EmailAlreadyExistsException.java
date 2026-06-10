package edu.softkau.dev.insurancebackend.customer.domain.exception;

import edu.softkau.dev.insurancebackend.customer.domain.model.Email;

public class EmailAlreadyExistsException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "El correo electrónico %s ya está registrado.";

    public EmailAlreadyExistsException(Email email) {
        super(String.format(MESSAGE_TEMPLATE, email.getValue()));
    }
}
