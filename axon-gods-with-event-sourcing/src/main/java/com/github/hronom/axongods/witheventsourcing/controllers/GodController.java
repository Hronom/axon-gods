package com.github.hronom.axongods.witheventsourcing.controllers;

import com.github.hronom.axongods.witheventsourcing.query.GodEntry;
import com.github.hronom.axongods.witheventsourcing.query.GodQueryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
public class GodController {
    private final GodQueryRepository godQueryRepository;

    @Autowired
    public GodController(GodQueryRepository godQueryRepository) {
        this.godQueryRepository = godQueryRepository;
    }

    @GetMapping(path = "/gods", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<GodEntry>> getAllHumans()
        throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(godQueryRepository.findAll());
    }
}
