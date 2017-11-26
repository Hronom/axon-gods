package com.github.hronom.axongods.witheventsourcing.configs;

import com.mongodb.MongoClient;

import org.axonframework.mongo.eventsourcing.eventstore.DefaultMongoTemplate;
import org.axonframework.mongo.eventsourcing.eventstore.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.UnknownHostException;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class PersistenceInfrastructureConfig {

    @Autowired
    private MongoProperties mongoProperties;

    @Bean
    public MongoTemplate eventStoreMongoTemplate(MongoClient mongoClient) throws UnknownHostException {
        return new DefaultMongoTemplate(
            mongoClient,
            mongoProperties.getDatabase(),
            "domains-events",
            "snapshot-events"
        );
    }

    @Bean
    public org.axonframework.mongo.eventhandling.saga.repository.MongoTemplate sagaMongoTemplate(MongoClient mongoClient)
        throws UnknownHostException {
        return new org.axonframework.mongo.eventhandling.saga.repository.DefaultMongoTemplate(
            mongoClient,
            mongoProperties.getDatabase(),
            "sagas"
        );
    }
}
