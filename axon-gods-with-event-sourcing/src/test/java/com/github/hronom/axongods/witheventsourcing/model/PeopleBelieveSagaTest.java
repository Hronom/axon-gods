package com.github.hronom.axongods.witheventsourcing.model;

import com.github.hronom.axongods.witheventsourcing.matchers.GodCreateCommandMatcher;
import com.github.hronom.axongods.witheventsourcing.matchers.GodIncreaseValueCommandMatcher;
import com.github.hronom.axongods.common.events.HumanBeganToBelieveEvent;

import org.axonframework.test.saga.SagaTestFixture;
import org.junit.Before;
import org.junit.Test;

import static org.axonframework.test.matchers.Matchers.exactSequenceOf;

public class PeopleBelieveSagaTest {
    private SagaTestFixture<PeopleBelieveSaga> fixture;

    @Before
    public void setUp() throws Exception {
        fixture = new SagaTestFixture<>(PeopleBelieveSaga.class);
    }

    @Test
    public void testCreation() throws Exception {
        fixture
            .whenAggregate("id-1")
            .publishes(
                new HumanBeganToBelieveEvent(
                    "id-1",
                    "god-name-1"
                )
            )
            .expectActiveSagas(1)
            .expectNoScheduledEvents()
            .expectDispatchedCommandsMatching(
                exactSequenceOf(
                    new GodCreateCommandMatcher(
                        "id-1",
                        "god-name-1"
                    )
                )
            );
    }

    @Test
    public void testCreationInitiatedByTwoInstancesOfAggregate1() throws Exception {
        fixture
            .givenAggregate("id-1")
            .published(
                new HumanBeganToBelieveEvent(
                    "id-1",
                    "god-name-1"
                )
            )
            .whenAggregate("id-2")
            .publishes(
                new HumanBeganToBelieveEvent(
                    "id-2",
                    "god-name-1"
                )
            )
            .expectActiveSagas(1)
            .expectNoScheduledEvents()
            .expectDispatchedCommandsMatching(
                exactSequenceOf(
                    new GodIncreaseValueCommandMatcher()
                )
            );
    }
}