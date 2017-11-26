package com.github.hronom.axongods.witheventsourcing.query;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GodQueryRepository extends MongoRepository<GodEntry, String> {
}
