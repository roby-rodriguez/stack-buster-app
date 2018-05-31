package com.robyrodriguez.stackbuster.service.worker.strategy;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robyrodriguez.stackbuster.client.StackClient;
import com.robyrodriguez.stackbuster.exception.StackResourceNotFoundException;
import com.robyrodriguez.stackbuster.service.RequestAnalyzerService;
import com.robyrodriguez.stackbuster.transfer.RequestAnalyzerDO;
import com.robyrodriguez.stackbuster.transfer.firebase.HistoryEntryDO;
import com.robyrodriguez.stackbuster.transfer.firebase.UserDO;
import com.robyrodriguez.stackbuster.transfer.firebase.UserWorkingQuestionDO;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;
import com.robyrodriguez.stackbuster.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Increments view counter for a question on behalf of supplied user:
 *
 * - maintains an unique IP address list to allow check for completion
 * - if nr. of views for required badge reached, question data moved to history
 * - if question meanwhile deleted by original SO owner, process is aborted
 */
@Component
public class UserQuestionStrategy implements IncrementStrategy<UserWorkingQuestionDO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserQuestionStrategy.class);

    @Autowired
    private RequestAnalyzerService requestAnalyzerService;

    @Autowired
    private StackClient stackClient;

    @Autowired
    private FirebaseDatabase database;

    @Override
    public void execute(UserWorkingQuestionDO question) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        BadgeType badge = question.getBadgeType();
        int clicks = question.getClicks();
        RequestAnalyzerDO address = requestAnalyzerService.update();
        List<String> addresses = question.getAddresses();

        // if current IP has already clicked the link, do nothing
        if (address != null && !addresses.contains(address.getIp())) {
            try {
                stackClient.incrementCounter(question.getId(), question.getUid());

                String completed = CommonUtil.getCompletionPercentage(++clicks, badge.getClicks());

                addresses.add(address.getIp());
                updates.put("addresses", addresses);
                updates.put("clicks", clicks);

                database.getReference("/questions/user/" + question.getId() + "/completed")
                        .setValueAsync(completed);
                database.getReference("/workingQuestions/user/" + question.getId())
                        .updateChildren(updates, (error, firebase) -> {
                            if (error != null) {
                                UserQuestionStrategy.LOGGER.warn("Could not update question progress for working question id {}"
                                        + " error {}", question.getId(), error);
                            } else if (CommonUtil.COMPLETED.equals(completed)) {
                                // if question completed, remove from `/workingQuestions` and add to history
                                database.getReference("/history/" + question.getId())
                                        .setValueAsync(new HistoryEntryDO<>(question, ProgressType.COMPLETED));
                                database.getReference("/workingQuestions/user/" + question.getId())
                                        .removeValueAsync();
                                // update user data
                                database.getReference("/users/" + question.getUser_id())
                                        .addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot snapshot) {
                                                Map<String, Object> updates = new HashMap<>();
                                                UserDO user = snapshot.getValue(UserDO.class);
                                                int activeQuestions = user.getActiveQuestions();
                                                updates.put("activeQuestions", activeQuestions - 1);
                                                database.getReference("/users/" + question.getId())
                                                        .updateChildrenAsync(updates);
                                            }
                                            @Override
                                            public void onCancelled(DatabaseError error) {
                                                UserQuestionStrategy.LOGGER.warn("Could not update user data for working question"
                                                        + " id {} error {}", question.getId(), error);
                                            }
                                        });
                            }
                        });
            } catch (StackResourceNotFoundException e) {
                UserQuestionStrategy.LOGGER.warn("Working question {} has been aborted due to {}", question, e);
                database.getReference("/history/" + question.getId())
                        .setValueAsync(new HistoryEntryDO<>(question, ProgressType.ABORTED));
                database.getReference("/workingQuestions/user/" + question.getId())
                        .removeValueAsync();
            }
        }
    }
}
