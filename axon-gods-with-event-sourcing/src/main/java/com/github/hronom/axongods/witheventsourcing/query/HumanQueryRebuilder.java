package com.github.hronom.axongods.witheventsourcing.query;

import com.github.hronom.axongods.witheventsourcing.eventhandling.OneTimeReplayerEventProcessor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.SimpleEventHandlerInvoker;
import org.axonframework.eventhandling.tokenstore.inmemory.InMemoryTokenStore;
import org.axonframework.eventsourcing.eventstore.EventStore;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class HumanQueryRebuilder implements InitializingBean {
    private final Log logger = LogFactory.getLog(getClass());

    private final EventStore eventStore;
    private final HumanQueryListener listener;
    private final TransactionManager transactionManager;

    @Autowired
    public HumanQueryRebuilder(
        EventStore eventStore,
        HumanQueryListener listener,
        TransactionManager transactionManager
    ) {
        this.eventStore = eventStore;
        this.listener = listener;
        this.transactionManager = transactionManager;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        OneTimeReplayerEventProcessor eventProcessor =
            new OneTimeReplayerEventProcessor(
                "humanQueryListenerEventProcessor",
                new SimpleEventHandlerInvoker(listener),
                eventStore,
                new InMemoryTokenStore(),
                transactionManager
            );
        eventProcessor.start();
        eventProcessor.shutDown();
    }
}
