package com.github.hronom.axongods.witheventsourcing.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hronom.axongods.common.events.HumanBeganToBelieveEvent;
import com.github.hronom.axongods.common.events.HumanCreatedEvent;
import com.github.hronom.axongods.common.events.HumanDeletedEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanQueryListener {
    private final Log logger = LogFactory.getLog(getClass());
    private final ObjectMapper om = new ObjectMapper();

    @Autowired
    private HumanQueryRepository humanQueryRepository;

    @EventHandler
    public void handle(HumanCreatedEvent event) throws JsonProcessingException {
        HumanEntry entry = new HumanEntry();
        entry.id = event.id;
        entry.name = event.name;
        humanQueryRepository.save(entry);

        logger.info(om.writeValueAsString(event));
    }

    @EventHandler
    public void handle(HumanBeganToBelieveEvent event) throws JsonProcessingException {
        HumanEntry entry = humanQueryRepository.findOne(event.id);
        entry.believeInGod = event.god;
        humanQueryRepository.save(entry);
        logger.info(om.writeValueAsString(event));
    }

    @EventHandler
    public void handle(HumanDeletedEvent event) throws JsonProcessingException {
        humanQueryRepository.delete(event.id);
        logger.info(om.writeValueAsString(event));
    }
}
