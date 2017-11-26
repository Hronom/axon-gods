package com.github.hronom.axongods.witheventsourcing.query;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanQueryRepository extends MongoRepository<HumanEntry, String> {
    boolean existsByName(String name);
    HumanEntry findByName(String name);
}
