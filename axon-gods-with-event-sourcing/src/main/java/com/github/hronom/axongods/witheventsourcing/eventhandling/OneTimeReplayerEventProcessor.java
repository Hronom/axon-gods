package com.github.hronom.axongods.witheventsourcing.eventhandling;

import org.axonframework.common.Assert;
import org.axonframework.common.transaction.TransactionManager;
import org.axonframework.eventhandling.AbstractEventProcessor;
import org.axonframework.eventhandling.ErrorHandler;
import org.axonframework.eventhandling.EventHandlerInvoker;
import org.axonframework.eventhandling.EventMessage;
import org.axonframework.eventhandling.PropagatingErrorHandler;
import org.axonframework.eventhandling.Segment;
import org.axonframework.eventhandling.TrackedEventMessage;
import org.axonframework.eventhandling.TrackingEventProcessor;
import org.axonframework.eventhandling.tokenstore.TokenStore;
import org.axonframework.eventhandling.tokenstore.UnableToClaimTokenException;
import org.axonframework.eventsourcing.eventstore.TrackingToken;
import org.axonframework.messaging.MessageStream;
import org.axonframework.messaging.StreamableMessageSource;
import org.axonframework.messaging.interceptors.TransactionManagingInterceptor;
import org.axonframework.messaging.unitofwork.BatchingUnitOfWork;
import org.axonframework.messaging.unitofwork.RollbackConfiguration;
import org.axonframework.messaging.unitofwork.RollbackConfigurationType;
import org.axonframework.monitoring.MessageMonitor;
import org.axonframework.monitoring.NoOpMessageMonitor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.requireNonNull;
import static org.axonframework.common.io.IOUtils.closeQuietly;

public class OneTimeReplayerEventProcessor extends AbstractEventProcessor {
    private static final Logger logger = LoggerFactory.getLogger(TrackingEventProcessor.class);

    private final StreamableMessageSource<TrackedEventMessage<?>> messageSource;
    private final TokenStore tokenStore;
    private final TransactionManager transactionManager;
    private final int batchSize;
    private volatile TrackingToken lastToken;

    /**
     * Initializes an EventProcessor with given {@code name} that subscribes to the given {@code
     * messageSource} for
     * events. Actual handling of event messages is deferred to the given {@code
     * eventHandlerInvoker}.
     * <p>
     * The EventProcessor is initialized with a batch size of 1, a {@link PropagatingErrorHandler},
     * a {@link
     * RollbackConfigurationType#ANY_THROWABLE} and a {@link NoOpMessageMonitor}.
     *
     * @param name                The name of the event processor
     * @param eventHandlerInvoker The component that handles the individual events
     * @param messageSource       The message source (e.g. Event Bus) which this event processor
     *                            will track
     * @param tokenStore          Used to store and fetch event tokens that enable the processor to
     *                            track its progress
     * @param transactionManager  The transaction manager used when processing messages
     */
    public OneTimeReplayerEventProcessor(
        String name,
        EventHandlerInvoker eventHandlerInvoker,
        StreamableMessageSource<TrackedEventMessage<?>> messageSource,
        TokenStore tokenStore,
        TransactionManager transactionManager
    ) {
        this(
            name,
            eventHandlerInvoker,
            messageSource,
            tokenStore,
            transactionManager,
            NoOpMessageMonitor.INSTANCE
        );
    }

    /**
     * Initializes an EventProcessor with given {@code name} that subscribes to the given {@code
     * messageSource} for
     * events. Actual handling of event messages is deferred to the given {@code
     * eventHandlerInvoker}.
     * <p>
     * The EventProcessor is initialized with a batch size of 1, a {@link PropagatingErrorHandler},
     * a {@link
     * RollbackConfigurationType#ANY_THROWABLE} and a {@link NoOpMessageMonitor}.
     *
     * @param name                The name of the event processor
     * @param eventHandlerInvoker The component that handles the individual events
     * @param messageSource       The message source (e.g. Event Bus) which this event processor
     *                            will track
     * @param tokenStore          Used to store and fetch event tokens that enable the processor to
     *                            track its progress
     * @param transactionManager  The transaction manager used when processing messages
     * @param batchSize           The maximum number of events to process in a single batch
     */
    public OneTimeReplayerEventProcessor(
        String name,
        EventHandlerInvoker eventHandlerInvoker,
        StreamableMessageSource<TrackedEventMessage<?>> messageSource,
        TokenStore tokenStore,
        TransactionManager transactionManager,
        int batchSize
    ) {
        this(
            name,
            eventHandlerInvoker,
            RollbackConfigurationType.ANY_THROWABLE,
            PropagatingErrorHandler.INSTANCE,
            messageSource,
            tokenStore,
            transactionManager,
            batchSize,
            NoOpMessageMonitor.INSTANCE
        );
    }

    /**
     * Initializes an EventProcessor with given {@code name} that subscribes to the given {@code
     * messageSource} for
     * events. Actual handling of event messages is deferred to the given {@code
     * eventHandlerInvoker}.
     * <p>
     * The EventProcessor is initialized with a batch size of 1, a {@link PropagatingErrorHandler}
     * and a {@link
     * RollbackConfigurationType#ANY_THROWABLE}.
     *
     * @param name                The name of the event processor
     * @param eventHandlerInvoker The component that handles the individual events
     * @param messageSource       The message source (e.g. Event Bus) which this event processor
     *                            will track
     * @param tokenStore          Used to store and fetch event tokens that enable the processor to
     *                            track its progress
     * @param transactionManager  The transaction manager used when processing messages
     * @param messageMonitor      Monitor to be invoked before and after event processing
     */
    public OneTimeReplayerEventProcessor(
        String name,
        EventHandlerInvoker eventHandlerInvoker,
        StreamableMessageSource<TrackedEventMessage<?>> messageSource,
        TokenStore tokenStore,
        TransactionManager transactionManager,
        MessageMonitor<? super EventMessage<?>> messageMonitor
    ) {
        this(name,
            eventHandlerInvoker,
            RollbackConfigurationType.ANY_THROWABLE,
            PropagatingErrorHandler.INSTANCE,
            messageSource,
            tokenStore,
            transactionManager,
            1,
            messageMonitor
        );
    }

    /**
     * Initializes an EventProcessor with given {@code name} that subscribes to the given {@code
     * messageSource} for
     * events. Actual handling of event messages is deferred to the given {@code
     * eventHandlerInvoker}.
     *
     * @param name                  The name of the event processor
     * @param eventHandlerInvoker   The component that handles the individual events
     * @param rollbackConfiguration Determines rollback behavior of the UnitOfWork while processing
     *                              a batch of events
     * @param errorHandler          Invoked when a UnitOfWork is rolled back during processing
     * @param messageSource         The message source (e.g. Event Bus) which this event processor
     *                              will track
     * @param tokenStore            Used to store and fetch event tokens that enable the processor
     *                              to track its
     *                              progress
     * @param transactionManager    The transaction manager used when processing messages
     * @param batchSize             The maximum number of events to process in a single batch
     * @param messageMonitor        Monitor to be invoked before and after event processing
     */
    public OneTimeReplayerEventProcessor(
        String name,
        EventHandlerInvoker eventHandlerInvoker,
        RollbackConfiguration rollbackConfiguration,
        ErrorHandler errorHandler,
        StreamableMessageSource<TrackedEventMessage<?>> messageSource,
        TokenStore tokenStore,
        TransactionManager transactionManager,
        int batchSize,
        MessageMonitor<? super EventMessage<?>> messageMonitor
    ) {
        super(name, eventHandlerInvoker, rollbackConfiguration, errorHandler, messageMonitor);
        Assert.isTrue(batchSize > 0, () -> "batchSize needs to be greater than 0");
        this.messageSource = requireNonNull(messageSource);
        this.tokenStore = requireNonNull(tokenStore);
        this.transactionManager = transactionManager;
        this.batchSize = batchSize;
        registerInterceptor(new TransactionManagingInterceptor<>(transactionManager));
        registerInterceptor((unitOfWork, interceptorChain) -> {
            unitOfWork.onPrepareCommit(uow -> {
                EventMessage<?> event = uow.getMessage();
                if (event instanceof TrackedEventMessage<?> && lastToken != null &&
                    lastToken.equals(((TrackedEventMessage) event).trackingToken())) {
                    tokenStore.storeToken(lastToken, getName(), 0);
                }
            });
            return interceptorChain.proceed();
        });
    }

    /**
     * Start this processor. The processor will open an event stream on its message source in a new
     * thread using {@link
     * StreamableMessageSource#openStream(TrackingToken)}. The {@link TrackingToken} used to open
     * the stream will be
     * fetched from the {@link TokenStore}.
     */
    @Override
    public void start() {
        try {
            this.processingLoop();
        } catch (Throwable e) {
            logger.error("Processing loop ended due to uncaught exception. Processor pausing.", e);
        }
    }

    /**
     * Fetch and process event batches continuously for as long as the processor is not shutting
     * down. The processor
     * will process events in batches. The maximum size of size of each event batch is
     * configurable.
     * <p>
     * Events with the same tracking token (which is possible as result of upcasting) should always
     * be processed in
     * the same batch. In those cases the batch size may be larger than the one configured.
     */
    protected void processingLoop() {
        long errorWaitTime = 1;
        try (MessageStream<TrackedEventMessage<?>> eventStream =
                 transactionManager
                     .fetchInTransaction(
                         () -> messageSource.openStream(tokenStore.fetchToken(getName(), 0))
                     )
        ) {
            while (eventStream.hasNextAvailable()) {
                try {
                    processBatch(eventStream);
                    errorWaitTime = 1;
                } catch (UnableToClaimTokenException e) {
                    if (errorWaitTime == 1) {
                        logger.info(
                            "Token is owned by another node. Waiting for it to become available...");
                    }
                    errorWaitTime = 5;
                    waitFor(errorWaitTime);
                } catch (Exception e) {
                    // make sure to start with a clean event stream. The exception may have cause an illegal state
                    if (errorWaitTime == 1) {
                        logger.warn("Error occurred. Starting retry mode.", e);
                    }
                    logger.warn("Releasing claim on token and preparing for retry in {}s",
                        errorWaitTime
                    );
                    releaseToken();
                    closeQuietly(eventStream);
                    waitFor(errorWaitTime);
                    errorWaitTime = Math.min(errorWaitTime * 2, 60);
                }
            }
        } finally {
            releaseToken();
        }
    }

    private void waitFor(long errorWaitTime) {
        try {
            Thread.sleep(errorWaitTime * 1000);
        } catch (InterruptedException e1) {
            shutDown();
            Thread.currentThread().interrupt();
            logger.warn("Thread interrupted. Preparing to shut down event processor");
        }
    }

    private void releaseToken() {
        try {
            transactionManager.executeInTransaction(() -> tokenStore.releaseClaim(getName(), 0));
        } catch (Exception e) {
            // whatever.
        }
    }

    private void processBatch(MessageStream<TrackedEventMessage<?>> eventStream) throws Exception {
        List<TrackedEventMessage<?>> batch = new ArrayList<>();
        try {
            if (eventStream.hasNextAvailable(1, TimeUnit.SECONDS)) {
                while (batch.size() < batchSize && eventStream.hasNextAvailable()) {
                    batch.add(eventStream.nextAvailable());
                }
            }
            if (batch.isEmpty()) {
                // refresh claim on token
                transactionManager.executeInTransaction(() -> tokenStore.extendClaim(getName(), 0));
                return;
            }

            // make sure all subsequent events with the same token (if non-null) as the last are added as well.
            // These are the result of upcasting and should always be processed in the same batch.
            lastToken = batch.get(batch.size() - 1).trackingToken();
            while (lastToken != null &&
                   eventStream.peek().filter(event -> lastToken.equals(event.trackingToken()))
                       .isPresent()) {
                batch.add(eventStream.nextAvailable());
            }

            processInUnitOfWork(batch, new BatchingUnitOfWork<>(batch), Segment.ROOT_SEGMENT);
        } catch (InterruptedException e) {
            logger.error(String
                .format("Event processor [%s] was interrupted. Shutting down.", getName()), e);
            this.shutDown();
            Thread.currentThread().interrupt();
        }
    }

    private MessageStream<TrackedEventMessage<?>> ensureEventStreamOpened(
        MessageStream<TrackedEventMessage<?>> eventStreamIn
    ) {
        MessageStream<TrackedEventMessage<?>> eventStream = eventStreamIn;
        if (eventStream == null) {
            eventStream = transactionManager.fetchInTransaction(() -> messageSource
                .openStream(tokenStore.fetchToken(getName(), 0)));
        }
        return eventStream;
    }

    /**
     * Shut down the processor.
     */
    @Override
    public void shutDown() {
    }
}
