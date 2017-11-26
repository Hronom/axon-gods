package com.github.hronom.axongods.common.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

public class HumanDeletedCommand {
    @TargetAggregateIdentifier
    private String id;

    public HumanDeletedCommand(String id) {
        Assert.notNull(id, () -> "The provided id cannot be null");
        this.id = id;
    }

    public String getId() {
        return id;
    }
}
