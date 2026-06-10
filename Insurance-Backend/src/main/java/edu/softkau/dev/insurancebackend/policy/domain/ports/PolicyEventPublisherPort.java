package edu.softkau.dev.insurancebackend.policy.domain.ports;

import edu.softkau.dev.insurancebackend.policy.domain.events.PolicyStatusChangedEvent;

public interface PolicyEventPublisherPort {
    void publish(PolicyStatusChangedEvent event);
}
