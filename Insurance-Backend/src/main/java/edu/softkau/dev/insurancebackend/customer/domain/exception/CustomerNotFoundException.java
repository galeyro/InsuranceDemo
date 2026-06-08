package edu.softkau.dev.insurancebackend.customer.domain.exception;

import edu.softkau.dev.insurancebackend.customer.domain.model.CustomerId;

public class CustomerNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "El cliente con ID %s no fue encontrado.";

    public CustomerNotFoundException(CustomerId id) {
        // Mensaje formateado dinámicamente antes de pasarlo al super
        super(String.format(MESSAGE_TEMPLATE, id.getValue()));
    }
}

