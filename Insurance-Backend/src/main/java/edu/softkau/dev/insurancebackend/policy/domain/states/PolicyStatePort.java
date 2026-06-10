package edu.softkau.dev.insurancebackend.policy.domain.states;

import edu.softkau.dev.insurancebackend.policy.domain.exception.InvalidStateTransitionException;
import edu.softkau.dev.insurancebackend.policy.domain.model.Policy;
import edu.softkau.dev.insurancebackend.policy.domain.model.PolicyStatus;

public abstract class PolicyStatePort {

    protected abstract PolicyStatus status();

    public final void transitionTo(Policy policy, PolicyStatus target){
        if (target == null){
            throw new IllegalArgumentException("El estado no puede ser nulo");
        }

        if (target == status()){
            return;
        }

        if (!canTransitionTo(target)){
            throw new InvalidStateTransitionException(status(), target);
        }

        policy.applyState(nextStateFor(target));
    }

    public final PolicyStatus getStatus(){
        return status();
    }

    protected abstract boolean canTransitionTo(PolicyStatus target);

    protected abstract PolicyStatePort nextStateFor(PolicyStatus target);
}
