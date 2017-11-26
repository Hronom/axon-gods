package com.github.hronom.axongods.withouteventsourcing.controllers.pojos;

public class HumanPojo {
    public final String id;
    public final String name;

    public final String believeInGod;

    public HumanPojo(String id, String name, String believeInGod) {
        this.id = id;
        this.name = name;
        this.believeInGod = believeInGod;
    }
}
