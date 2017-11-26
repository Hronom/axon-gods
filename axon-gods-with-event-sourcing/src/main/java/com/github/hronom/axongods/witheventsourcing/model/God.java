package com.github.hronom.axongods.witheventsourcing.model;

import com.github.hronom.axongods.common.commands.GodCreateCommand;
import com.github.hronom.axongods.common.commands.GodDecreaseValueCommand;
import com.github.hronom.axongods.common.commands.GodDeleteCommand;
import com.github.hronom.axongods.common.commands.GodIncreaseValueCommand;
import com.github.hronom.axongods.common.events.GodCreatedEvent;
import com.github.hronom.axongods.common.events.GodDeletedEvent;
import com.github.hronom.axongods.common.events.GodValueDecreasedEvent;
import com.github.hronom.axongods.common.events.GodValueIncreasedEvent;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serializable;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class God implements Serializable {
    @AggregateIdentifier
    private String id;
    private String name;

    private int value;

    public God() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Commands handlers
    @CommandHandler
    public God(GodCreateCommand command) {
        apply(new GodCreatedEvent(command.id, command.name));
    }

    @CommandHandler
    public void delete(GodDeleteCommand command) {
        apply(new GodDeletedEvent(command.getId(), name));
    }

    @CommandHandler
    public void increaseValue(GodIncreaseValueCommand command) {
        apply(new GodValueIncreasedEvent(id));
    }

    @CommandHandler
    public void decreaseValue(GodDecreaseValueCommand command) {
        apply(new GodValueDecreasedEvent(id));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Events handlers
    @EventSourcingHandler
    public void on(GodCreatedEvent event) {
        this.id = event.id;
        this.name = event.name;
        this.value = 1;
    }

    @EventSourcingHandler
    public void on(GodDeletedEvent event) {
        markDeleted();
    }

    @EventSourcingHandler
    public void on(GodValueIncreasedEvent event) {
        this.value += 1;
    }

    @EventSourcingHandler
    public void on(GodValueDecreasedEvent event) {
        this.value -= 1;
    }
}
