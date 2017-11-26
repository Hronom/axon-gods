package com.github.hronom.axongods.witheventsourcing.query;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "god_entry")
public class GodEntry {
    @Id
    public String id;
    @Indexed
    public String name;

    public int value;
}
