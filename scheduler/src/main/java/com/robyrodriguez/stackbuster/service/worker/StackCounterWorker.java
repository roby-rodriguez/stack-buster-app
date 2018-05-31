package com.robyrodriguez.stackbuster.service.worker;

import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.service.RequestAnalyzerService;
import com.robyrodriguez.stackbuster.service.worker.dispatcher.IncrementDispatcher;
import com.robyrodriguez.stackbuster.transfer.firebase.AbstractWorkingQuestionDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map.Entry;

/**
 * Increments question counters and manages data read from `/workingQuestions`
 */
@Component
public class StackCounterWorker implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackCounterWorker.class);

    @Autowired
    private StackBusterCache cache;

    @Autowired
    private RequestAnalyzerService requestAnalyzerService;

    @Autowired
    private IncrementDispatcher incrementDispatcher;

    @Override
    public void run() {
        StackCounterWorker.LOGGER.info("started scheduled conter-incrementer process");
        for (Entry<String, AbstractWorkingQuestionDO> entry : cache.entries()) {
            AbstractWorkingQuestionDO question = entry.getValue();

            try {
                incrementDispatcher.dispatch(question);
            } catch (Exception e) {
                StackCounterWorker.LOGGER.error("cannot increment counter for user question {}", question, e);
            }
        }

        // allow reset of current address in use - TODO check if you annotate run with evict also works
        requestAnalyzerService.reset();
    }
}
