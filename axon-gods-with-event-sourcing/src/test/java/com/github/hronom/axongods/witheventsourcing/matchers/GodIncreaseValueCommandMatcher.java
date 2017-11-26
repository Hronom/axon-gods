package com.github.hronom.axongods.witheventsourcing.matchers;

import com.github.hronom.axongods.common.commands.GodIncreaseValueCommand;

import org.hamcrest.Description;

public class GodIncreaseValueCommandMatcher extends BaseCommandMatcher<GodIncreaseValueCommand> {

    public GodIncreaseValueCommandMatcher() {
    }

    @Override
    protected boolean doMatches(GodIncreaseValueCommand commandMessage) {
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description
            .appendText(this.getClass().getSimpleName() + " [")
            .appendText("]");
    }
}
