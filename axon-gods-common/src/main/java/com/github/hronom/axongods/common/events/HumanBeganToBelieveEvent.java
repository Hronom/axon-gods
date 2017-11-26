package com.github.hronom.axongods.common.events;

public class HumanBeganToBelieveEvent {
    public final String id;
    public final String god;

    public HumanBeganToBelieveEvent(String id, String god) {
        this.id = id;
        this.god = god;
    }
}