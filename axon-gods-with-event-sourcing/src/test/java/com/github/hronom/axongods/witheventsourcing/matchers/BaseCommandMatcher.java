package com.github.hronom.axongods.witheventsourcing.matchers;

import org.axonframework.commandhandling.CommandMessage;
import org.hamcrest.BaseMatcher;

public abstract class BaseCommandMatcher<T> extends BaseMatcher<CommandMessage<?>> {
    @SuppressWarnings("unchecked")
    @Override
    public final boolean matches(Object o) {
        if (!(o instanceof CommandMessage)) {
            return false;
        }
        CommandMessage<T> message = (CommandMessage<T>) o;
        return doMatches(message.getPayload());
    }

    protected abstract boolean doMatches(T commandMessage);
}
