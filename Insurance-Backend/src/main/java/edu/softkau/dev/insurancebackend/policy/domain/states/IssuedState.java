package edu.softkau.dev.insurancebackend.policy.domain.states;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;

public class IssuedState extends PolicyStatePort{
    @Override
    protected PolicyStatus status() {
        return PolicyStatus.ISSUED;
    }

    @Override
    protected boolean canTransitionTo(PolicyStatus target) {
        return target == PolicyStatus.ACTIVE || target == PolicyStatus.CANCELLED;
    }

    @Override
    protected PolicyStatePort nextStateFor(PolicyStatus target) {
        return switch (target){
            case ACTIVE -> new ActiveState();
            case CANCELLED -> new CancelledState();
            default -> null;
        };
    }
}
