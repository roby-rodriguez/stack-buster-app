package com.robyrodriguez.stackbuster.service;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.annotation.StackBusterData;
import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener.ListenerType;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.service.worker.StackCounterWorker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

/**
 * Main service
 */
@Service
@StackBusterData(structureType = StructureType.INHERITANCE)
public class StackBusterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackBusterService.class);

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${tasks.increment-counters.timeout:6000}")
    private long incrementCountersTimeout;

    @Value("${tasks.cache-loading.timeout:10000}")
    private long cacheLoadingTimeout;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private FirebaseDatabase database;

    @Autowired
    private StackBusterCache cache;

    @StackBusterListener(type = ListenerType.QUESTION)
    private ChildEventListener questionsListener;

    @StackBusterListener(type = ListenerType.USER_QUESTION)
    private ChildEventListener userQuestionsListener;

    @StackBusterListener(type = ListenerType.WORKING_QUESTION)
    private ChildEventListener workingQuestionsListener;

    @StackBusterListener(type = ListenerType.WORKING_USER_QUESTION)
    private ChildEventListener userWorkingQuestionsListener;

    @Autowired
    private StackCounterWorker stackCounterWorker;

    /**
     * Starts batch processing of questions
     *
     * @throws Exception
     */
    @PostConstruct
    public void start() throws Exception {
        /* caching section */
        this.database.getReference("/workingQuestions/default").addChildEventListener(workingQuestionsListener);
        this.database.getReference("/workingQuestions/user").addChildEventListener(userWorkingQuestionsListener);

        /* wait for loading then spit out cache */
        Thread.sleep(cacheLoadingTimeout);
        StackBusterService.LOGGER.info("cache loaded: {}", cache);

        /* sanitizer section */
        this.database.getReference("/questions/default").addChildEventListener(questionsListener);
        this.database.getReference("/questions/user").addChildEventListener(userQuestionsListener);

        Thread.sleep(cacheLoadingTimeout);

        /* scheduler section */
        this.taskScheduler.scheduleWithFixedDelay(stackCounterWorker, this.incrementCountersTimeout);
    }
}
