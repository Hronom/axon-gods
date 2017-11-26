package com.github.hronom.axongods.witheventsourcing.query;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "human_entry")
public class HumanEntry {
    @Id
    public String id;
    @Indexed
    public String name;

    public String believeInGod;
}
