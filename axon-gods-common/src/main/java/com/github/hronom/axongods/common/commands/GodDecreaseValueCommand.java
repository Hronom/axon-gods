package com.github.hronom.axongods.common.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

public class GodDecreaseValueCommand {
    @TargetAggregateIdentifier
    public final String id;

    public GodDecreaseValueCommand(String id) {
        Assert.notNull(id, () -> "The provided id cannot be null");
        this.id = id;
    }
}
