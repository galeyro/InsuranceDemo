package edu.softkau.dev.insurancebackend.policy.domain.exception;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;

public class InvalidStateTransitionException extends RuntimeException {
    public InvalidStateTransitionException(PolicyStatus source, PolicyStatus target) {
        super("No es posible transicionar el estado de la póliza desde " + source + " a " + target);
    }
}
