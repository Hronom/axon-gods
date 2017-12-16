package com.github.hronom.axongods.withouteventsourcing.configs;

import org.axonframework.eventhandling.EventBus;
import org.axonframework.eventhandling.SimpleEventBus;
import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoSagaStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CqrsInfrastructureMongoDbConfig {
    @Autowired
    private DefaultMongoTemplate defaultMongoTemplate;

    @Bean
    public EventBus eventBus() {
        return new SimpleEventBus();
    }

    @Bean
    public MongoSagaStore sagaRepository() {
        return new MongoSagaStore(defaultMongoTemplate);
    }
}
