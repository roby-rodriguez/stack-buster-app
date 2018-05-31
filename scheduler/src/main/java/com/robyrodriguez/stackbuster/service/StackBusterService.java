package com.robyrodriguez.stackbuster.service;

import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.service.listener.QuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.UserQuestionsListener;
import com.robyrodriguez.stackbuster.service.listener.WorkingQuestionsListener;
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

    @Autowired
    private QuestionsListener questionsListener;

    @Autowired
    private UserQuestionsListener userQuestionsListener;

    @Autowired
    private WorkingQuestionsListener workingQuestionsListener;

    @Autowired
    private WorkingQuestionsListener userWorkingQuestionsListener;

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
