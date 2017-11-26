package com.github.hronom.axongods.common.commands;

import org.axonframework.commandhandling.TargetAggregateIdentifier;
import org.axonframework.common.Assert;

public class HumanBeganToBelieveCommand {
    @TargetAggregateIdentifier
    public final String id;
    public final String god;

    public HumanBeganToBelieveCommand(
        String id,
        String god
    ) {
        Assert.notNull(id, () -> "The provided id cannot be null");
        Assert.notNull(god, () -> "The provided god cannot be null");

        this.id = id;
        this.god = god;
    }
}
