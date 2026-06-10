package edu.softkau.dev.insurancebackend.policy.domain.states;

import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;

public class QuotedState  extends PolicyStatePort{
    @Override
    protected PolicyStatus status() {
        return PolicyStatus.QUOTED;
    }

    @Override
    protected boolean canTransitionTo(PolicyStatus target) {
        return target == PolicyStatus.ISSUED || target == PolicyStatus.CANCELLED;
    }

    @Override
    protected PolicyStatePort nextStateFor(PolicyStatus target) {
        return switch (target){
            case ISSUED -> new IssuedState();
            case CANCELLED -> new CancelledState();
            default -> null;
        };
    }
}
