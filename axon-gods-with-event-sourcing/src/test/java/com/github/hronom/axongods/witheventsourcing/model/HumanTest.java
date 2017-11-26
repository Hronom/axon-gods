package com.github.hronom.axongods.witheventsourcing.model;

import com.github.hronom.axongods.common.commands.HumanBeganToBelieveCommand;
import com.github.hronom.axongods.common.commands.HumanCreateCommand;
import com.github.hronom.axongods.common.commands.HumanDeletedCommand;
import com.github.hronom.axongods.common.events.HumanBeganToBelieveEvent;
import com.github.hronom.axongods.common.events.HumanBelieverDeletedEvent;
import com.github.hronom.axongods.common.events.HumanCreatedEvent;
import com.github.hronom.axongods.common.events.HumanDeletedEvent;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class HumanTest {
    private AggregateTestFixture<Human> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(Human.class);
    }

    @Test
    public void testCreated() throws Exception {
        fixture
            .givenNoPriorActivity()
            .when(
                new HumanCreateCommand(
                    "id-1",
                    "name-1"
                )
            )
            .expectEvents(
                new HumanCreatedEvent(
                    "id-1",
                    "name-1"
                )
            );
    }

    @Test
    public void testBeganToBelieve() throws Exception {
        fixture
            .given(
                new HumanCreatedEvent(
                    "id-1",
                    "name-1"
                )
            )
            .when(
                new HumanBeganToBelieveCommand(
                    "id-1",
                    "god-name-1"
                )
            )
            .expectEvents(
                new HumanBeganToBelieveEvent(
                    "id-1",
                    "god-name-1"
                )
            );
    }

    @Test
    public void testNonBelieverDeleted() throws Exception {
        fixture
            .given(
                new HumanCreatedEvent(
                    "id-1",
                    "name-1"
                )
            )
            .when(
                new HumanDeletedCommand(
                    "id-1"
                )
            )
            .expectEvents(
                new HumanDeletedEvent(
                    "id-1"
                )
            );
    }

    @Test
    public void testBelieverDeleted() throws Exception {
        fixture
            .given(
                new HumanCreatedEvent(
                    "id-1",
                    "name-1"
                ),
                new HumanBeganToBelieveEvent(
                    "id-1",
                    "god-name-1"
                )
            )
            .when(
                new HumanDeletedCommand(
                    "id-1"
                )
            )
            .expectEvents(
                new HumanBelieverDeletedEvent(
                    "id-1",
                    "god-name-1"
                ),
                new HumanDeletedEvent(
                    "id-1"
                )
            );
    }
}