package com.github.hronom.axongods.witheventsourcing.configs;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventsourcing.eventstore.EmbeddedEventStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.mongo.MongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.axonframework.mongo.eventsourcing.eventstore.MongoEventStorageEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CqrsInfrastructureMongoDbConfig {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Bean
    public EventBus eventBus() {
        return eventStore();
    }

    @Bean
    public EventStore eventStore() {
        return new EmbeddedEventStore(eventStorageEngine());
    }

    @Bean
    public MongoEventStorageEngine eventStorageEngine() {
        return new MongoEventStorageEngine(mongoTemplate);
    }

    @Bean
    public MongoSagaStore sagaStore() {
        return new MongoSagaStore(mongoTemplate);
    }
}
