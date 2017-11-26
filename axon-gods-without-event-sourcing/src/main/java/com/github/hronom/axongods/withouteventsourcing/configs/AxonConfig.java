package com.github.hronom.axongods.withouteventsourcing.configs;

import com.github.hronom.axongods.withouteventsourcing.axon.AggregateMongoRepository;
import com.github.hronom.axongods.withouteventsourcing.model.God;
import com.github.hronom.axongods.withouteventsourcing.model.Human;
import com.github.hronom.axongods.withouteventsourcing.repositories.GodRepository;
import com.github.hronom.axongods.withouteventsourcing.repositories.HumanRepository;

import org.axonframework.common.lock.NullLockFactory;
import org.axonframework.eventhandling.EventBus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Bean
    public AggregateMongoRepository<Human> humanAggregateRepository(
        HumanRepository humanRepository,
        EventBus eventBus
    ) {
        return new AggregateMongoRepository<>(
            humanRepository,
            Human.class,
            eventBus,
            NullLockFactory.INSTANCE
        );
    }

    @Bean
    public AggregateMongoRepository<God> godAggregateRepository(
        GodRepository godRepository,
        EventBus eventBus
    ) {
        return new AggregateMongoRepository<>(
            godRepository,
            God.class,
            eventBus,
            NullLockFactory.INSTANCE
        );
    }
}
