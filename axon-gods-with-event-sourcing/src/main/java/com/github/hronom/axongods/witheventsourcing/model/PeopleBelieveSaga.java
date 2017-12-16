package com.github.hronom.axongods.witheventsourcing.model;

import com.github.hronom.axongods.common.commands.GodCreateCommand;
import com.github.hronom.axongods.common.commands.GodDecreaseValueCommand;
import com.github.hronom.axongods.common.commands.GodDeleteCommand;
import com.github.hronom.axongods.common.commands.GodIncreaseValueCommand;
import com.github.hronom.axongods.common.events.GodDeletedEvent;
import com.github.hronom.axongods.common.events.HumanBeganToBelieveEvent;
import com.github.hronom.axongods.common.events.HumanBelieverDeletedEvent;

import org.axonframework.commandhandling.CommandBus;
import org.axonframework.commandhandling.callbacks.LoggingCallback;
import org.axonframework.common.IdentifierFactory;
import org.axonframework.eventhandling.saga.EndSaga;
import org.axonframework.eventhandling.saga.SagaEventHandler;
import org.axonframework.eventhandling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.LinkedHashSet;

import static org.axonframework.commandhandling.GenericCommandMessage.asCommandMessage;

@Saga
public class PeopleBelieveSaga {
    private String godAggregateId;
    private LinkedHashSet<String> believersIds = new LinkedHashSet<>();

    private transient CommandBus commandBus;

    @Autowired
    public void setCommandBus(CommandBus commandBus) {
        this.commandBus = commandBus;
    }

    @StartSaga
    @SagaEventHandler(associationProperty = "god")
    public void on(HumanBeganToBelieveEvent event) {
        if (believersIds.isEmpty()) {
            godAggregateId = IdentifierFactory.getInstance().generateIdentifier();
            commandBus.dispatch(
                asCommandMessage(
                    new GodCreateCommand(godAggregateId, event.god)
                )
            );
        } else {
            commandBus.dispatch(
                asCommandMessage(
                    new GodIncreaseValueCommand(godAggregateId)
                )
            );
        }
        believersIds.add(event.id);

    }

    @SagaEventHandler(associationProperty = "god")
    public void on(HumanBelieverDeletedEvent event) {
        believersIds.remove(event.id);
        if (believersIds.isEmpty()) {
            commandBus.dispatch(
                asCommandMessage(
                    new GodDeleteCommand(godAggregateId)
                )
            );
        } else {
            commandBus.dispatch(
                asCommandMessage(
                    new GodDecreaseValueCommand(godAggregateId)
                )
            );
        }
    }

    @SagaEventHandler(associationProperty = "name")
    @EndSaga
    public void on(GodDeletedEvent event) {
    }
}
