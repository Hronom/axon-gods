package com.github.hronom.axongods.common.events;

public class GodDeletedEvent {
    public final String id;
    public final String name;

    public GodDeletedEvent(String id, String name) {
        this.id = id;
        this.name = name;
    }
}