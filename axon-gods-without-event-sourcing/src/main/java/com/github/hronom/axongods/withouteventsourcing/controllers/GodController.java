package com.github.hronom.axongods.withouteventsourcing.controllers;

import com.github.hronom.axongods.withouteventsourcing.controllers.pojos.GodPojo;
import com.github.hronom.axongods.withouteventsourcing.repositories.GodRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@RestController
public class GodController {
    private final GodRepository godRepository;

    @Autowired
    public GodController(GodRepository godRepository) {
        this.godRepository = godRepository;
    }

    @GetMapping(path = "/gods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<GodPojo>> getAllHumans()
        throws ExecutionException, InterruptedException {
        List<GodPojo> godPojos =
            godRepository
                .findAll()
                .stream()
                .map(g -> new GodPojo(g.getId(), g.getName(), g.getValue()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(godPojos);
    }
}
