package com.github.hronom.axongods.withouteventsourcing.repositories;

import com.github.hronom.axongods.withouteventsourcing.model.God;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GodRepository extends MongoRepository<God, String> {
}
