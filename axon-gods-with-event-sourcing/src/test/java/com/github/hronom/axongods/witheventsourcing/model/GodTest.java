package com.github.hronom.axongods.witheventsourcing.model;

import com.github.hronom.axongods.common.commands.GodCreateCommand;
import com.github.hronom.axongods.common.commands.GodDecreaseValueCommand;
import com.github.hronom.axongods.common.commands.GodIncreaseValueCommand;
import com.github.hronom.axongods.common.events.GodCreatedEvent;
import com.github.hronom.axongods.common.events.GodValueDecreasedEvent;
import com.github.hronom.axongods.common.events.GodValueIncreasedEvent;

import org.axonframework.test.aggregate.AggregateTestFixture;
import org.junit.Before;
import org.junit.Test;

public class GodTest {
    private AggregateTestFixture<God> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new AggregateTestFixture<>(God.class);
    }

    @Test
    public void testCreated() throws Exception {
        fixture
            .givenNoPriorActivity()
            .when(
                new GodCreateCommand(
                    "id-1",
                    "name-1"
                )
            )
            .expectEvents(
                new GodCreatedEvent(
                    "id-1",
                    "name-1"
                )
            );
    }

    @Test
    public void testGodValueIncreasedEvent() throws Exception {
        fixture
            .given(
                new GodCreatedEvent(
                    "id-1",
                    "name-1"
                )
            )
            .when(
                new GodIncreaseValueCommand("id-1")
            )
            .expectEvents(
                new GodValueIncreasedEvent("id-1")
            );
    }

    @Test
    public void testGodValueDecreasedEvent() throws Exception {
        fixture
            .given(
                new GodCreatedEvent(
                    "id-1",
                    "name-1"
                )
            )
            .when(
                new GodDecreaseValueCommand("id-1")
            )
            .expectEvents(
                new GodValueDecreasedEvent("id-1")
            );
    }
}