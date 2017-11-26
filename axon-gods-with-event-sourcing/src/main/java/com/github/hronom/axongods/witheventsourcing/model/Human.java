package com.github.hronom.axongods.witheventsourcing.model;

import com.github.hronom.axongods.common.commands.HumanBeganToBelieveCommand;
import com.github.hronom.axongods.common.commands.HumanCreateCommand;
import com.github.hronom.axongods.common.commands.HumanDeletedCommand;
import com.github.hronom.axongods.common.events.HumanBeganToBelieveEvent;
import com.github.hronom.axongods.common.events.HumanBelieverDeletedEvent;
import com.github.hronom.axongods.common.events.HumanCreatedEvent;
import com.github.hronom.axongods.common.events.HumanDeletedEvent;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.AggregateIdentifier;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.spring.stereotype.Aggregate;

import java.io.Serializable;
import java.util.Objects;

import static org.axonframework.commandhandling.model.AggregateLifecycle.apply;
import static org.axonframework.commandhandling.model.AggregateLifecycle.markDeleted;

@Aggregate
public class Human implements Serializable {
    @AggregateIdentifier
    private String id;
    private String name;

    private String believeInGod;

    public Human() {
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Commands handlers
    @CommandHandler
    public Human(HumanCreateCommand command) {
        apply(new HumanCreatedEvent(command.id, command.name));
    }

    @CommandHandler
    public void beganToBelieve(HumanBeganToBelieveCommand command) {
        apply(new HumanBeganToBelieveEvent(command.id, command.god));
    }

    @CommandHandler
    public void delete(HumanDeletedCommand command) {
        if (Objects.nonNull(believeInGod)) {
            apply(new HumanBelieverDeletedEvent(command.getId(), believeInGod));
        }
        apply(new HumanDeletedEvent(command.getId()));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // Events handlers
    @EventSourcingHandler
    public void on(HumanCreatedEvent event) {
        this.id = event.id;
        this.name = event.name;
    }

    @EventSourcingHandler
    public void on(HumanBeganToBelieveEvent event) {
        this.believeInGod = event.god;
    }

    @EventSourcingHandler
    public void on(HumanDeletedEvent event) {
        markDeleted();
    }
}
