package com.github.hronom.axongods.witheventsourcing.controllers;

import com.github.hronom.axongods.common.commands.HumanBeganToBelieveCommand;
import com.github.hronom.axongods.common.commands.HumanCreateCommand;
import com.github.hronom.axongods.common.commands.HumanDeletedCommand;
import com.github.hronom.axongods.witheventsourcing.query.HumanEntry;
import com.github.hronom.axongods.witheventsourcing.query.HumanQueryRepository;

import org.apache.commons.lang.exception.ExceptionUtils;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@RestController
public class HumanController {
    private final HumanQueryRepository humanQueryRepository;
    private final CommandGateway commandGateway;

    @Autowired
    public HumanController(
        HumanQueryRepository humanQueryRepository,
        CommandGateway commandGateway
    ) {
        this.humanQueryRepository = humanQueryRepository;
        this.commandGateway = commandGateway;
    }

    @PostMapping(path = "/humans", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> createHuman(
        @RequestParam(value = "id", required = true) String id,
        @RequestParam(value = "name", required = true) String name
    ) throws ExecutionException, InterruptedException {
        try {
            if (!humanQueryRepository.exists(id)) {
                HumanCreateCommand command = new HumanCreateCommand(
                    id,
                    name
                );
                CompletableFuture completableFuture = commandGateway.send(command);
                completableFuture.get();
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.badRequest().body("id already exists.");
            }
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(ExceptionUtils.getFullStackTrace(exception));
        }
    }

    @GetMapping(path = "/humans", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<List<HumanEntry>> getAllHumans()
        throws ExecutionException, InterruptedException {
        return ResponseEntity.ok(humanQueryRepository.findAll());
    }

    @PutMapping(path = "/humans/{id}/believe", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> humanBeginBelieve(
        @PathVariable(value = "id", required = true) String id,
        @RequestParam(value = "god", required = true) String god
    ) throws ExecutionException, InterruptedException {
        try {
            if (humanQueryRepository.exists(id)) {
                HumanBeganToBelieveCommand command = new HumanBeganToBelieveCommand(
                    id,
                    god
                );
                CompletableFuture completableFuture = commandGateway.send(command);
                completableFuture.get();
                return ResponseEntity.ok("ok");
            } else {
                return ResponseEntity.badRequest().body("human with id not exists.");
            }
        } catch (Exception exception){
            return ResponseEntity.badRequest().body(ExceptionUtils.getFullStackTrace(exception));
        }
    }

    @GetMapping(path = "/humans/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<HumanEntry> getHumans(
        @PathVariable(value = "id", required = true) String id
    ) throws ExecutionException, InterruptedException {
        HumanEntry humanEntry = humanQueryRepository.findOne(id);
        return ResponseEntity.ok(humanEntry);
    }

    @DeleteMapping(path = "/humans/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> deleteHuman(
        @PathVariable(value = "id", required = true) String id
    ) throws ExecutionException, InterruptedException {
        if (humanQueryRepository.exists(id)) {
            HumanDeletedCommand command = new HumanDeletedCommand(
                id
            );
            CompletableFuture completableFuture = commandGateway.send(command);
            completableFuture.get();
            return ResponseEntity.ok("ok");
        } else {
            return ResponseEntity.badRequest().body("human with id not exists.");
        }
    }
}
