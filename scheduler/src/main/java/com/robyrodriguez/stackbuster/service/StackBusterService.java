package com.robyrodriguez.stackbuster.service;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.client.StackClient;
import com.robyrodriguez.stackbuster.transfer.AbstractStackItemWrapperDO;
import com.robyrodriguez.stackbuster.transfer.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import com.robyrodriguez.stackbuster.transfer.StackQuestionDO;
import com.robyrodriguez.stackbuster.transfer.StackQuestionWrapperDO;
import com.robyrodriguez.stackbuster.transfer.StackUserWrapperDO;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;
import com.robyrodriguez.stackbuster.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.PostConstruct;
import javax.ws.rs.BadRequestException;

/**
 * Main service
 */
@Component
public class StackBusterService {

    private static final Logger LOGGER = LoggerFactory.getLogger(StackBusterService.class);

    @Value("${name:World}")
    private String name;

    @Value("${firebase.database.url}")
    private String databaseUrl;

    @Value("${tasks.increment-counters.timeout:6000}")
    private long incrementCountersTimeout;

    @Autowired
    private DefaultClient defaultClient;

    @Autowired
    private StackClient stackClient;

    @Autowired
    private ThreadPoolTaskScheduler taskScheduler;

    @Autowired
    private FirebaseDatabase database;

    // FIXME use spring boot caching
    /**
     * https://spring.io/guides/gs/caching/
     * http://www.baeldung.com/spring-cache-tutorial
     */
    private Map<String, QuestionDO> cache = new ConcurrentHashMap<>();

    public String getMessage() {
        try {
            RequestAnalyzerDO request = defaultClient.ping();
            LOGGER.info(request.toString());
        } catch (Exception e) {
            LOGGER.warn("Encountered error: {}", e);
        }
        return "Hello " + this.name;
    }

    /**
     * Starts batch processing of questions
     *
     * @throws Exception
     */
    @PostConstruct
    public void start() throws Exception {
        /* caching section */
        this.database.getReference("/workingQuestions").addChildEventListener(new ChildEventListener() {
            // FIXME refactor to separate class
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                QuestionDO question = dataSnapshot.getValue(QuestionDO.class);
                question.setId(dataSnapshot.getKey());
                if (ProgressType.IN_PROGRESS.equals(question.getProgress())) {
                    cache.put(question.getId(), question);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                QuestionDO question = dataSnapshot.getValue(QuestionDO.class);
                question.setId(dataSnapshot.getKey());
                if (ProgressType.IN_PROGRESS.equals(question.getProgress())) {
                    cache.put(question.getId(), question);
                } else {
                    cache.remove(dataSnapshot.getKey());
                }
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                cache.remove(dataSnapshot.getKey());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                StackBusterService.LOGGER.warn("onChildMoved called on '/workingQuestions' with args key={} value={}",
                        dataSnapshot.getKey(), dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                StackBusterService.LOGGER.warn("onCancelled called on '/workingQuestions' with error={}", databaseError);
            }
        });

        Thread.sleep(10000);
        System.out.println("cache:");
        System.out.println(cache);

        /* sanitizer section */
        this.database.getReference("/questions").addChildEventListener(new ChildEventListener() {
            // FIXME refactor to separate class
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                QuestionDO question = dataSnapshot.getValue(QuestionDO.class);
                question.setId(dataSnapshot.getKey());

                try {
                    if (cache.get(question.getId()) == null && question.getBadgeType() != null) {
                        // lookup original question and in case it does not exist, remove this "question" (garbage)
                        // TODO watch out if question has an answer with :id = qid server will fetch that question
                        // TODO https://stackoverflow.com/q/9999921 returns https://stackoverflow.com/questions/9982788/code-sign-error-no-unexpired-provisioning-profiles-found-that-contain-any-of-th/9999921#9999921
                        AbstractStackItemWrapperDO<StackQuestionDO> questionWrapper = defaultClient
                                .get(StackApi.QUESTION(question.getId()), StackQuestionWrapperDO.class);

                        if (questionWrapper.getItems().size() > 0) {
                            StackQuestionDO stackQuestion = questionWrapper.getItems().get(0);

                            if (question.getUid() != null) {
                                AbstractStackItemWrapperDO userWrapper = defaultClient
                                        .get(StackApi.USER(question.getUid()), StackUserWrapperDO.class);

                                if (userWrapper.getItems().size() > 0) {
                                    QuestionDO existing = cache.get(question.getId());

                                    if (existing == null) {
                                        database.getReference("/workingQuestions/" + question.getId())
                                                .setValueAsync(new QuestionDO(question, stackQuestion.getView_count()));
                                    }
                                } else {
                                    database.getReference("/questions/" + question.getId()).removeValueAsync();
                                }
                            } else {
                                QuestionDO existing = cache.get(question.getId());

                                if (existing == null) {
                                    database.getReference("/workingQuestions/" + question.getId())
                                            .setValueAsync(new QuestionDO(question, stackQuestion.getView_count()));
                                }
                            }
                        } else {
                            database.getReference("/questions/" + question.getId()).removeValueAsync();
                        }
                    }
                } catch (BadRequestException bre) {
                    database.getReference("/questions/" + question.getId()).removeValueAsync();
                } catch (Exception e) {
                    StackBusterService.LOGGER.error("Encountered error during sanitize {}", e);
                }
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                StackBusterService.LOGGER.warn("onChildChanged called on '/questions' with args key={} value={}",
                        dataSnapshot.getKey(), dataSnapshot.getValue());
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                StackBusterService.LOGGER.info("onChildRemoved called on '/questions' with args key={} value={}",
                        dataSnapshot.getKey(), dataSnapshot.getValue());
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
                StackBusterService.LOGGER.warn("onChildMoved called on '/questions' with args key={} value={}",
                        dataSnapshot.getKey(), dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                StackBusterService.LOGGER.warn("onCancelled called on '/questions' with error={}", databaseError);
            }
        });

        /* scheduler section */
        this.taskScheduler.scheduleWithFixedDelay(() -> {
            // FIXME refactor to separate class
            RequestAnalyzerDO requestAnalyzer = null;
            try {
                requestAnalyzer = stackClient.ping();
                StackBusterService.LOGGER.info("running stack scheduler with proxy address {}", requestAnalyzer.getIp());
            } catch (Exception e) {
                StackBusterService.LOGGER.error("cannot reach ping server", e);
            }

            for (Map.Entry<String, QuestionDO> entry : cache.entrySet()) {
                QuestionDO question = entry.getValue();

                try {
                    if (question.getBadgeType().isUserInvolved()) {
                        if (requestAnalyzer != null) {
                            updateCountersForUserQuestion(question, requestAnalyzer.getIp());
                        }
                    } else {
                        // TODO if no user involved
                        updateCountersForQuestion(question);
                    }
                } catch (Exception e) {
                    StackBusterService.LOGGER.error("cannot increment counter for user question {}", question, e);
                }
            }
        }, this.incrementCountersTimeout);
    }

    private void updateCountersForUserQuestion(QuestionDO question, String address) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        BadgeType badge = question.getBadgeType();
        int clicks = question.getClicks();
        List<String> addresses = question.getAddresses();

        // if current IP has already clicked the link, do nothing
        if (!addresses.contains(address)) {
            // TODO this question might have also been deleted -> see updateCountersForQuestion
            boolean ok = stackClient.incrementCounter(question.getId(), question.getUid());

            if (ok) {
                String completed = CommonUtil.getCompletionPercentage(clicks, badge.getClicks());

                addresses.add(address);
                updates.put("addresses", addresses);
                updates.put("clicks", clicks + 1);
                if (CommonUtil.COMPLETED.equals(completed)) {
                    updates.put("progress", ProgressType.COMPLETED);
                }

                database.getReference("/workingQuestions/" + question.getId())
                        .updateChildrenAsync(updates);
                database.getReference("/questions/" + question.getId() + "/completed")
                        .setValueAsync(completed);
            }
        }
    }

    // TODO de vazut daca fac handling la exceptie inauntru sau o dau mai departe in afara
    private void updateCountersForQuestion(QuestionDO question) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        BadgeType badge = question.getBadgeType();
        int clicks = question.getClicks();

        // TODO de vazut aici ce arunca cand ii dau un link cu intrebare stearsa
        boolean ok = stackClient.incrementCounter(question.getId(), null);

        if (ok) {
            // lookup original question and update knowledge based on current action
            AbstractStackItemWrapperDO<StackQuestionDO> questionWrapper = stackClient.get(
                    StackApi.QUESTION(question.getId()), StackQuestionWrapperDO.class);

            if (questionWrapper.getItems().size() > 0) {
                StackQuestionDO stackQuestion = questionWrapper.getItems().get(0);
                int currentViews = stackQuestion.getView_count();
                String completed = CommonUtil.getCompletionPercentage(currentViews, badge.getClicks());

                updates.put("clicks", clicks + 1);
                updates.put("currentViews", currentViews);
                if (CommonUtil.COMPLETED.equals(completed)) {
                    updates.put("progress", ProgressType.COMPLETED);
                }

                database.getReference("/workingQuestions/" + question.getId())
                        .updateChildrenAsync(updates);
                database.getReference("/questions/" + question.getId() + "/completed")
                        .setValueAsync(completed);
            } else {
                // question probably meanwhile deleted
                updates.put("progress", ProgressType.ABORTED);
                database.getReference("/workingQuestions/" + question.getId())
                        .updateChildrenAsync(updates);
            }
        }
    }
}
