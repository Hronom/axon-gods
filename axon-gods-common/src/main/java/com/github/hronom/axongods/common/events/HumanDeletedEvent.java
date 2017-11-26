package com.github.hronom.axongods.common.events;

public class HumanDeletedEvent {
    public final String id;

    public HumanDeletedEvent(String id) {
        this.id = id;
    }
}