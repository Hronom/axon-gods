package com.github.hronom.axongods.common.events;

public class GodCreatedEvent {
    public final String id;
    public final String name;

    public GodCreatedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }
}