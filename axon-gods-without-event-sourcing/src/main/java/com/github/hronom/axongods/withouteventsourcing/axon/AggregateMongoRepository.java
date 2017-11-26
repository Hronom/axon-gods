package com.github.hronom.axongods.withouteventsourcing.axon;

import org.axonframework.commandhandling.model.AggregateNotFoundException;
import org.axonframework.commandhandling.model.LockingRepository;
import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;
import org.axonframework.common.Assert;
import org.axonframework.common.lock.LockFactory;
import org.axonframework.eventhandling.EventBus;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.concurrent.Callable;

import static java.lang.String.format;

public class AggregateMongoRepository<T> extends LockingRepository<T, AnnotatedAggregate<T>> {
    private static final String aggregateCacheName = "aggregate_cache";

    private final MongoRepository<T, String> repository;
    private final EventBus eventBus;

    public AggregateMongoRepository(
        MongoRepository<T, String> repository,
        Class<T> aggregateType,
        EventBus eventBus,
        LockFactory lockFactory
    ) {
        super(aggregateType, lockFactory);
        Assert.notNull(repository, () -> "repository may not be null");
        Assert.notNull(eventBus, () -> "eventBus may not be null");
        this.repository = repository;
        this.eventBus = eventBus;
    }

    @Cacheable(value = aggregateCacheName, key = "#aggregateIdentifier")
    @Override
    protected AnnotatedAggregate<T> doLoadWithLock(String aggregateIdentifier, Long expectedVersion) {
        T aggregateRoot = repository.findOne(aggregateIdentifier);
        if (aggregateRoot == null) {
            throw new AggregateNotFoundException(aggregateIdentifier,
                format("Aggregate [%s] with identifier [%s] not found",
                    getAggregateType().getSimpleName(), aggregateIdentifier));
        }
        return AnnotatedAggregate.initialize(aggregateRoot, aggregateModel(), eventBus);
    }

    @Override
    protected AnnotatedAggregate<T> doCreateNewForLock(Callable<T> factoryMethod) throws Exception {
        return AnnotatedAggregate.initialize(factoryMethod, aggregateModel(), eventBus);
    }

    @CacheEvict(value = aggregateCacheName, key = "#aggregate.identifier().toString()")
    @Override
    protected void doSaveWithLock(AnnotatedAggregate<T> aggregate) {
        repository.save(aggregate.getAggregateRoot());
    }

    @CacheEvict(value = aggregateCacheName, key = "#aggregate.identifier().toString()")
    @Override
    protected void doDeleteWithLock(AnnotatedAggregate<T> aggregate) {
        repository.delete(aggregate.getAggregateRoot());
    }
}
