package com.robyrodriguez.stackbuster.service;

import com.robyrodriguez.stackbuster.annotation.StackBusterData;
import com.robyrodriguez.stackbuster.annotation.StackBusterData.StructureType;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener;
import com.robyrodriguez.stackbuster.annotation.StackBusterListener.ListenerType;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.database.DatabaseManager;
import com.robyrodriguez.stackbuster.service.listener.DefaultQuestionsListener;
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
    private DatabaseManager database;

    @Autowired
    private StackBusterCache cache;

    @StackBusterListener(type = ListenerType.QUESTION_DEFAULT)
    private DefaultQuestionsListener defaultQuestionsListener;

    @StackBusterListener(type = ListenerType.QUESTION_USER)
    private UserQuestionsListener userQuestionsListener;

    @StackBusterListener
    private QuestionsListener questionsListener;

    @StackBusterListener(type = ListenerType.WORKING_QUESTION_DEFAULT)
    private WorkingQuestionsListener defaultWorkingQuestionsListener;

    @StackBusterListener(type = ListenerType.WORKING_QUESTION_USER)
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
        this.database.registerListeners(
            defaultWorkingQuestionsListener,
            userWorkingQuestionsListener
        );

        /* wait for loading then spit out cache */
        Thread.sleep(cacheLoadingTimeout);
        StackBusterService.LOGGER.info("cache loaded: {}", cache);

        /* sanitizer section */
        this.database.registerListener(questionsListener);
        this.database.registerListener(defaultQuestionsListener);
        this.database.registerListener(userQuestionsListener);

        Thread.sleep(cacheLoadingTimeout);

        /* scheduler section */
        this.taskScheduler.scheduleWithFixedDelay(stackCounterWorker, this.incrementCountersTimeout);
    }
}
