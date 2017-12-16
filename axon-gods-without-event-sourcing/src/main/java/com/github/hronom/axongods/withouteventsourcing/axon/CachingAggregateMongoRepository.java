package com.github.hronom.axongods.withouteventsourcing.axon;

import org.axonframework.commandhandling.model.inspection.AnnotatedAggregate;
import org.axonframework.common.caching.Cache;
import org.axonframework.common.lock.LockFactory;
import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.AggregateDeletedException;
import org.axonframework.messaging.unitofwork.CurrentUnitOfWork;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.concurrent.Callable;

public class CachingAggregateMongoRepository<T> extends AggregateMongoRepository<T> {
    private final Cache cache;

    public CachingAggregateMongoRepository(
        MongoRepository<T, String> repository,
        Class<T> aggregateType,
        EventBus eventBus,
        LockFactory lockFactory,
        Cache cache
    ) {
        super(repository, aggregateType, eventBus, lockFactory);
        this.cache = cache;
    }

    @Override
    protected AnnotatedAggregate<T> doLoadWithLock(String aggregateIdentifier, Long expectedVersion) {
        AnnotatedAggregate<T> aggregate = null;
        AggregateCacheEntry<T> cacheEntry = cache.get(aggregateIdentifier);
        if (cacheEntry != null) {
            aggregate = cacheEntry.getAggregate();
        }
        if (aggregate == null) {
            aggregate = super.doLoadWithLock(aggregateIdentifier, expectedVersion);
        } else if (aggregate.isDeleted()) {
            throw new AggregateDeletedException(aggregateIdentifier);
        }
        CurrentUnitOfWork.get().onRollback(u -> cache.remove(aggregateIdentifier));
        return aggregate;
    }

    @Override
    protected AnnotatedAggregate<T> doCreateNewForLock(Callable<T> factoryMethod) throws Exception {
        AnnotatedAggregate<T> aggregate = super.doCreateNewForLock(factoryMethod);
        String aggregateIdentifier = aggregate.identifierAsString();
        cache.put(aggregateIdentifier, new AggregateCacheEntry<>(aggregate));
        return aggregate;
    }

    @Override
    protected void doSaveWithLock(AnnotatedAggregate<T> aggregate) {
        super.doSaveWithLock(aggregate);
        cache.put(aggregate.identifierAsString(), new AggregateCacheEntry<>(aggregate));
    }

    @Override
    protected void doDeleteWithLock(AnnotatedAggregate<T> aggregate) {
        super.doDeleteWithLock(aggregate);
        cache.put(aggregate.identifierAsString(), new AggregateCacheEntry<>(aggregate));
    }
}
