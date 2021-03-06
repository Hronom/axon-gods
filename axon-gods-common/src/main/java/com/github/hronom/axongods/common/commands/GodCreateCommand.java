package com.github.hronom.axongods.common.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

public class GodCreateCommand {
    @TargetAggregateIdentifier
    public final String id;
    public final String name;

    public GodCreateCommand(
        String id,
        String name
    ) {
        Assert.notNull(id, () -> "The provided id cannot be null");
        Assert.notNull(name, () -> "The provided name cannot be null");

        this.id = id;
        this.name = name;
    }
}
