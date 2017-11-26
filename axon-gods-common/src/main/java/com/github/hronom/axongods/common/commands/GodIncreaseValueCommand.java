package com.github.hronom.axongods.common.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

public class GodIncreaseValueCommand {
    @TargetAggregateIdentifier
    public final String id;

    public GodIncreaseValueCommand(String id) {
        Assert.notNull(id, () -> "The provided id cannot be null");

        this.id = id;
    }
}
