package com.github.hronom.axongods.common.events;

public class HumanBelieverDeletedEvent {
    public final String id;
    public final String god;

    public HumanBelieverDeletedEvent(String id, String god) {
        this.id = id;
        this.god = god;
    }
}