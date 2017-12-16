package com.github.hronom.axongods.withouteventsourcing.configs;

import com.github.hronom.axongods.withouteventsourcing.axon.CachingAggregateMongoRepository;
import com.github.hronom.axongods.withouteventsourcing.model.God;
import com.github.hronom.axongods.withouteventsourcing.model.Human;
import com.github.hronom.axongods.withouteventsourcing.repositories.GodRepository;
import com.github.hronom.axongods.withouteventsourcing.repositories.HumanRepository;

import net.sf.ehcache.CacheManager;

import org.axonframework.common.caching.EhCacheAdapter;
import org.axonframework.common.lock.NullLockFactory;
import org.axonframework.eventhandling.EventBus;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AxonConfig {
    @Bean
    public EhCacheAdapter humanEhCache(CacheManager cacheManager) {
        return new EhCacheAdapter(cacheManager.addCacheIfAbsent("humanEhCache"));
    }

    @Bean
    public EhCacheAdapter godEhCache(CacheManager cacheManager) {
        return new EhCacheAdapter(cacheManager.addCacheIfAbsent("godEhCache"));
    }

    @Bean
    public EhCacheManagerFactoryBean ehCacheManagerFactoryBean() {
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setShared(true);
        return ehCacheManagerFactoryBean;
    }

    @Bean
    public CachingAggregateMongoRepository<Human> humanAggregateRepository(
        HumanRepository humanRepository,
        EventBus eventBus,
        EhCacheAdapter humanEhCache
    ) {
        return new CachingAggregateMongoRepository<>(
            humanRepository,
            Human.class,
            eventBus,
            NullLockFactory.INSTANCE,
            humanEhCache
        );
    }

    @Bean
    public CachingAggregateMongoRepository<God> godAggregateRepository(
        GodRepository godRepository,
        EventBus eventBus,
        EhCacheAdapter godEhCache
    ) {
        return new CachingAggregateMongoRepository<>(
            godRepository,
            God.class,
            eventBus,
            NullLockFactory.INSTANCE,
            godEhCache
        );
    }
}
