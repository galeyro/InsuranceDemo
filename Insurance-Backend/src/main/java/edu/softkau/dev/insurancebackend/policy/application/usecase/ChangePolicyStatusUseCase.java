package edu.softkau.dev.insurancebackend.policy.application.usecase;

import edu.softkau.dev.insurancebackend.policy.domain.events.PolicyStatusChangedEvent;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;
import edu.softkau.dev.insurancebackend.policy.domain.ports.PolicyEventPublisherPort;
import org.springframework.stereotype.Service;

@Service
public class ChangePolicyStatusUseCase {
    private final PolicyEventPublisherPort eventPublisher;

    // Spring inyectará automáticamente el adaptador de Kafka que crearemos más adelante
    public ChangePolicyStatusUseCase(PolicyEventPublisherPort eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    public void execute(Policy policy, PolicyStatus targetStatus){
        PolicyStatus oldStatus = policy.getStatus();

        policy.changeStatus(targetStatus);

        if (oldStatus != policy.getStatus()) {
            String eventType = determineEventType(oldStatus, policy.getStatus());

            PolicyStatusChangedEvent event = new PolicyStatusChangedEvent(
                    eventType,
                    policy.getId().toString(),
                    policy.getPolicyNumber(),
                    policy.getCustomerId().toString(),
                    policy.getBranch(),
                    oldStatus,
                    policy.getStatus(),
                    policy.getUpdatedAt()
            );

            eventPublisher.publish(event);
        }
    }

    private String determineEventType(PolicyStatus oldStatus, PolicyStatus newStatus) {
        if (newStatus == PolicyStatus.CANCELLED) {
            return "policy.cancelled";
        }
        if (oldStatus == PolicyStatus.QUOTED && newStatus == PolicyStatus.ISSUED) {
            return "policy.issued";
        }
        if (oldStatus == PolicyStatus.ISSUED && newStatus == PolicyStatus.ACTIVE) {
            return "policy.activated";
        }
        if (oldStatus == PolicyStatus.ACTIVE && newStatus == PolicyStatus.SUSPENDED) {
            return "policy.suspended";
        }
        if (oldStatus == PolicyStatus.SUSPENDED && newStatus == PolicyStatus.ACTIVE) {
            return "policy.reactivated";
        }
        return "policy.status_changed";
    }
}
