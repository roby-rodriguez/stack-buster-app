package com.robyrodriguez.stackbuster.service.worker.strategy;

import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.client.StackClient;
import com.robyrodriguez.stackbuster.exception.StackResourceNotFoundException;
import com.robyrodriguez.stackbuster.transfer.firebase.HistoryEntryDO;
import com.robyrodriguez.stackbuster.transfer.firebase.WorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.AbstractStackItemWrapperDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionWrapperDO;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;
import com.robyrodriguez.stackbuster.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Increments view counter for a question:
 *
 * - checks current views after each increment and updates `/workingQuestions` progress
 * - if nr. of views for required badge reached, question data moved to history
 * - if question meanwhile deleted by original SO owner, process is aborted
 */
@Component
public class QuestionStrategy implements IncrementStrategy<WorkingQuestionDO> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionStrategy.class);

    @Autowired
    private StackClient stackClient;

    @Autowired
    private FirebaseDatabase database;

    @Override
    public void execute(WorkingQuestionDO question) throws Exception {
        Map<String, Object> updates = new HashMap<>();
        BadgeType badge = question.getBadgeType();
        int clicks = question.getClicks();

        try {
            stackClient.incrementCounter(question.getId());

            // lookup original question and update knowledge based on current action
            AbstractStackItemWrapperDO<StackQuestionDO> questionWrapper = stackClient.get(
                    StackApi.QUESTION(question.getId()), StackQuestionWrapperDO.class);

            if (questionWrapper.getItems().size() > 0) {
                StackQuestionDO stackQuestion = questionWrapper.getItems().get(0);
                int currentViews = stackQuestion.getView_count();
                String completed = CommonUtil.getCompletionPercentage(currentViews, badge.getClicks());

                updates.put("currentViews", currentViews);
                updates.put("clicks", clicks + 1);

                database.getReference("/questions/default/" + question.getId() + "/completed")
                        .setValueAsync(completed);
                database.getReference("/workingQuestions/default/" + question.getId())
                        .updateChildren(updates, (error, firebase) -> {
                            if (error != null) {
                                QuestionStrategy.LOGGER.warn("Could not update question progress for working question id {}",
                                        question.getId());
                            } else if (CommonUtil.COMPLETED.equals(completed)) {
                                database.getReference("/history/" + question.getId())
                                        .setValueAsync(new HistoryEntryDO<>(question, ProgressType.COMPLETED));
                                database.getReference("/workingQuestions/default/" + question.getId())
                                        .removeValueAsync();
                            }
                        });
            } else {
                //FIXME maybe remove method and move this to catch -> see refactoring resolution() in StackClient
                abort(question);
            }
        } catch (StackResourceNotFoundException e) {
            QuestionStrategy.LOGGER.warn("Working question {} has been aborted due to {}", question, e);
            abort(question);
        }
    }

    /**
     * Question probably meanwhile deleted -> abort & move to history
     *
     * @param question current question
     */
    private void abort(WorkingQuestionDO question) {
        database.getReference("/history/" + question.getId())
                .setValueAsync(new HistoryEntryDO<>(question, ProgressType.ABORTED));
        database.getReference("/workingQuestions/default/" + question.getId())
                .removeValueAsync();
    }
}
