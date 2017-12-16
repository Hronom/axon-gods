package com.github.hronom.axongods.witheventsourcing.configs;

import com.mongodb.MongoClient;

import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
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
    public MongoTemplate mongoTemplate(MongoClient mongoClient) throws UnknownHostException {
        return new DefaultMongoTemplate(
            mongoClient,
            mongoProperties.getDatabase()
        );
    }
}
