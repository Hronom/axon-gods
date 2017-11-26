package com.github.hronom.axongods.withouteventsourcing.runner;

import com.github.hronom.axongods.common.commands.HumanBeganToBelieveCommand;
import com.github.hronom.axongods.common.commands.HumanCreateCommand;
import com.github.hronom.axongods.withouteventsourcing.repositories.HumanRepository;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.common.IdentifierFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class AxonGodsApplicationRunner implements ApplicationRunner {
    private final CommandGateway commandGateway;
    private final HumanRepository humanRepository;

    @Autowired
    public AxonGodsApplicationRunner(
        CommandGateway commandGateway,
        HumanRepository humanRepository
    ) {
        this.commandGateway = commandGateway;
        this.humanRepository = humanRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if (!humanRepository.existsByName("Jon 1")) {
            String id = IdentifierFactory.getInstance().generateIdentifier();
            {
                HumanCreateCommand command = new HumanCreateCommand(id, "Jon 1");
                CompletableFuture completableFuture = commandGateway.send(command);
                completableFuture.get();
            }

            {
                HumanBeganToBelieveCommand command =
                    new HumanBeganToBelieveCommand(id, "Phone God");
                CompletableFuture completableFuture = commandGateway.send(command);
                completableFuture.get();
            }
        }
    }
}
