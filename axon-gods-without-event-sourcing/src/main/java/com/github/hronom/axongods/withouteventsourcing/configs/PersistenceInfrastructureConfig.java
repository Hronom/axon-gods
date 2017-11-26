package com.github.hronom.axongods.withouteventsourcing.configs;

import com.mongodb.MongoClient;

import org.axonframework.mongo.eventhandling.saga.repository.DefaultMongoTemplate;
import org.axonframework.mongo.eventhandling.saga.repository.MongoTemplate;
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
    public MongoTemplate sagaMongoTemplate(MongoClient mongoClient)
        throws UnknownHostException {
        return new DefaultMongoTemplate(
            mongoClient,
            mongoProperties.getDatabase(),
            "sagas"
        );
    }
}
