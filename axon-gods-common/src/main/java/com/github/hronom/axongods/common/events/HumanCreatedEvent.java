package com.github.hronom.axongods.common.events;

public class HumanCreatedEvent {
    public final String id;
    public final String name;

    public HumanCreatedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }
}