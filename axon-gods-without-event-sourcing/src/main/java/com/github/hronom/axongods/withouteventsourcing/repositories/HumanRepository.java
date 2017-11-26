package com.github.hronom.axongods.withouteventsourcing.repositories;

import com.github.hronom.axongods.withouteventsourcing.model.Human;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HumanRepository extends MongoRepository<Human, String> {
    boolean existsByName(String name);
    Human findByName(String name);
}
