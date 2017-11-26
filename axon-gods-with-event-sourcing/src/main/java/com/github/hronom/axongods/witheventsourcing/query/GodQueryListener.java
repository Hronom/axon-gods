package com.github.hronom.axongods.witheventsourcing.query;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.hronom.axongods.common.events.GodCreatedEvent;
import com.github.hronom.axongods.common.events.GodDeletedEvent;
import com.github.hronom.axongods.common.events.GodValueDecreasedEvent;
import com.github.hronom.axongods.common.events.GodValueIncreasedEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GodQueryListener {
    private final Log logger = LogFactory.getLog(getClass());
    private final ObjectMapper om = new ObjectMapper();

    @Autowired
    private GodQueryRepository godQueryRepository;

    @EventHandler
    public void handle(GodCreatedEvent event) throws JsonProcessingException {
        GodEntry entry = new GodEntry();
        entry.id = event.id;
        entry.name = event.name;
        entry.value = 1;
        godQueryRepository.save(entry);
        logger.info(om.writeValueAsString(event));
    }

    @EventHandler
    public void handle(GodDeletedEvent event) throws JsonProcessingException {
        godQueryRepository.delete(event.id);
        logger.info(om.writeValueAsString(event));
    }

    @EventHandler
    public void handle(GodValueIncreasedEvent event) throws JsonProcessingException {
        GodEntry entry = godQueryRepository.findOne(event.id);
        entry.value += 1;
        godQueryRepository.save(entry);
        logger.info(om.writeValueAsString(event));
    }

    @EventHandler
    public void handle(GodValueDecreasedEvent event) throws JsonProcessingException {
        GodEntry entry = godQueryRepository.findOne(event.id);
        entry.value -= 1;
        godQueryRepository.save(entry);
        logger.info(om.writeValueAsString(event));
    }
}
