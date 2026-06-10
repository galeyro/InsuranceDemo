package edu.softkau.dev.insurancebackend.policy.domain.states;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;

public class ActiveState extends PolicyStatePort{
    @Override
    protected PolicyStatus status() {
        return PolicyStatus.ACTIVE;
    }

    @Override
    protected boolean canTransitionTo(PolicyStatus target) {
        return target == PolicyStatus.SUSPENDED || target == PolicyStatus.CANCELLED;
    }

    @Override
    protected PolicyStatePort nextStateFor(PolicyStatus target) {
        return switch (target){
            case SUSPENDED -> new SuspendedState();
            case CANCELLED -> new CancelledState();
            default -> null;
        };
    }
}
