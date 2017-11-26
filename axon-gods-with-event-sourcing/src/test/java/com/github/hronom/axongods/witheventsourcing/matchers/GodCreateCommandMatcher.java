package com.github.hronom.axongods.witheventsourcing.matchers;

import com.github.hronom.axongods.common.commands.GodCreateCommand;

import org.hamcrest.Description;

import java.util.Objects;

public class GodCreateCommandMatcher extends BaseCommandMatcher<GodCreateCommand> {
    private final String name;

    public GodCreateCommandMatcher(String id, String name) {
        this.name = name;
    }

    @Override
    protected boolean doMatches(GodCreateCommand commandMessage) {
        return Objects.equals(commandMessage.name, name);
    }

    @Override
    public void describeTo(Description description) {
        description
            .appendText(this.getClass().getSimpleName() + " [")
            .appendValue(name)
            .appendText("]");
    }
}
