package com.github.hronom.axongods.withouteventsourcing.axon;

import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;

import java.io.Serializable;

public class AggregateCacheEntry<T> implements Serializable {

    private final T aggregateRoot;
    private final Long version;

    private final transient AnnotatedAggregate<T> aggregate;

    public AggregateCacheEntry(AnnotatedAggregate<T> aggregate) {
        this.aggregate = aggregate;
        this.aggregateRoot = aggregate.getAggregateRoot();
        this.version = aggregate.version();
    }

    public AnnotatedAggregate<T> getAggregate() {
        return aggregate;
    }

    public Long getVersion() {
        return version;
    }
}