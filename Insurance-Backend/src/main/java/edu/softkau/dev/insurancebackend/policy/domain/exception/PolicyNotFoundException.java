package edu.softkau.dev.insurancebackend.policy.domain.exception;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyId;

public class PolicyNotFoundException extends RuntimeException {

    private static final String MESSAGE_TEMPLATE = "La póliza con ID %s no fue encontrada.";

    public PolicyNotFoundException(PolicyId id) {
        super(String.format(MESSAGE_TEMPLATE, id.getValue()));
    }
}
