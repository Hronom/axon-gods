package com.github.hronom.axongods.witheventsourcing.configs;

import com.github.hronom.axongods.witheventsourcing.model.God;
import com.github.hronom.axongods.witheventsourcing.model.Human;

import net.sf.ehcache.CacheManager;

import org.axonframework.commandhandling.model.Repository;
import org.axonframework.common.caching.EhCacheAdapter;
import org.axonframework.eventsourcing.AggregateFactory;
import org.axonframework.eventsourcing.CachingEventSourcingRepository;
import org.axonframework.eventsourcing.EventCountSnapshotTriggerDefinition;
import org.axonframework.eventsourcing.Snapshotter;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.axonframework.spring.eventsourcing.SpringAggregateSnapshotterFactoryBean;
import org.axonframework.spring.eventsourcing.SpringPrototypeAggregateFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Autowired
    private Snapshotter snapshotter;

    @Bean
    public SpringAggregateSnapshotterFactoryBean springAggregateSnapshotterFactoryBean() {
        return new SpringAggregateSnapshotterFactoryBean();
    }

    @Bean
    public EhCacheAdapter ehCache(CacheManager cacheManager) {
        return new EhCacheAdapter(cacheManager.addCacheIfAbsent("ehCache"));
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public Repository<Human> humanRepository(EventStore eventStore, EhCacheAdapter ehCacheAdapter) {
        EventCountSnapshotTriggerDefinition snapshotTriggerDefinition =
            new EventCountSnapshotTriggerDefinition(
                snapshotter,
                3
            );

        return new CachingEventSourcingRepository<>(
            humanAggregateFactory(),
            eventStore,
            ehCacheAdapter,
            snapshotTriggerDefinition
        );
    }

    @Bean
    public AggregateFactory<Human> humanAggregateFactory() {
        SpringPrototypeAggregateFactory<Human> springPrototypeAggregateFactory =
            new SpringPrototypeAggregateFactory<>();
        springPrototypeAggregateFactory.setPrototypeBeanName("human");
        return springPrototypeAggregateFactory;
    }

    @Bean
    public Repository<God> godRepository(EventStore eventStore, EhCacheAdapter ehCacheAdapter) {
        EventCountSnapshotTriggerDefinition snapshotTriggerDefinition =
            new EventCountSnapshotTriggerDefinition(
                snapshotter,
                3
            );

        return new CachingEventSourcingRepository<>(
            godAggregateFactory(),
            eventStore,
            ehCacheAdapter,
            snapshotTriggerDefinition
        );
    }

    @Bean
    public AggregateFactory<God> godAggregateFactory() {
        SpringPrototypeAggregateFactory<God> springPrototypeAggregateFactory =
            new SpringPrototypeAggregateFactory<>();
        springPrototypeAggregateFactory.setPrototypeBeanName("god");
        return springPrototypeAggregateFactory;
    }
}
