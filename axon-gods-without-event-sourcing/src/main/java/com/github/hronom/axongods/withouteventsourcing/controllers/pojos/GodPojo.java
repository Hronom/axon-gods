package com.github.hronom.axongods.withouteventsourcing.controllers.pojos;

public class GodPojo {
    public final String id;
    public final String name;

    public final int value;

    public GodPojo(String id, String name, int value) {
        this.id = id;
        this.name = name;
        this.value = value;
    }
}
