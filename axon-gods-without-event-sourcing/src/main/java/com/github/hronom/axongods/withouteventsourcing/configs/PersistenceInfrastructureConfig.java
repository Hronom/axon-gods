package com.github.hronom.axongods.withouteventsourcing.configs;

import com.mongodb.MongoClient;

import org.axonframework.mongo.DefaultMongoTemplate;
import org.axonframework.mongo.MongoTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.mongo.MongoProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(MongoProperties.class)
public class PersistenceInfrastructureConfig {

    @Autowired
    private MongoProperties mongoProperties;

    @Autowired
    private MongoClient mongoClient;

    @Bean
    public DefaultMongoTemplate defaultMongoTemplate() {
        return new DefaultMongoTemplate(
            mongoClient,
            mongoProperties.getDatabase()
        );
    }
}
