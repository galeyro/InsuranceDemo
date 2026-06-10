package edu.softkau.dev.insurancebackend.policy.domain.states;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;

public class CancelledState extends PolicyStatePort{
    @Override
    protected PolicyStatus status() {
        return PolicyStatus.CANCELLED;
    }

    @Override
    protected boolean canTransitionTo(PolicyStatus target) {
        // No transitions from cancelled
        return false;
    }

    @Override
    protected PolicyStatePort nextStateFor(PolicyStatus target) {
        return null;
    }
}
