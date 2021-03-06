package com.robyrodriguez.stackbuster.service.worker.strategy;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.client.StackClient;
import com.robyrodriguez.stackbuster.constants.DatabasePaths;
import com.robyrodriguez.stackbuster.exception.StackResourceNotFoundException;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultWorkingQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.others.HistoryEntryDO;
import com.robyrodriguez.stackbuster.transfer.firebase.others.UserDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.AbstractStackItemWrapperDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionWrapperDO;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;
import com.robyrodriguez.stackbuster.utils.CommonUtil;
import com.robyrodriguez.stackbuster.utils.MapBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Increments view counter for a default question:
 *
 * - checks current views after each increment and updates `/workingQuestions` progress
 * - if nr. of views for required badge reached, question data moved to history
 * - if question meanwhile deleted by original SO owner, process is aborted
 */
@Component
public class DefaultQuestionStrategy implements IncrementStrategy<DefaultWorkingQuestion> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultQuestionStrategy.class);

    @Autowired
    private StackClient stackClient;

    @Autowired
    private DatabasePaths paths;

    @Autowired
    private FirebaseDatabase database;

    @Override
    public void execute(DefaultWorkingQuestion question) throws Exception {
        BadgeType badge = question.getBadgeType();

        try {
            stackClient.incrementCounter(question.getId());

            // lookup original question and update knowledge based on current action
            AbstractStackItemWrapperDO<StackQuestionDO> questionWrapper = stackClient.get(
                    StackApi.QUESTION(question.getId()), StackQuestionWrapperDO.class);

            if (questionWrapper.getItems().size() > 0) {
                StackQuestionDO stackQuestion = questionWrapper.getItems().get(0);
                int currentViews = stackQuestion.getView_count();
                String completed = CommonUtil.getCompletionPercentage(currentViews, badge.getClicks());
                Map<String, Object> updates = updateQuestion(question, currentViews);

                database.getReference(paths.resolve(this).questionsCompleted(question.getId()))
                        .setValueAsync(completed);
                database.getReference(paths.resolve(this).workingQuestions(question.getId()))
                        .updateChildren(updates, (error, firebase) -> {
                            if (error != null) {
                                DefaultQuestionStrategy.LOGGER.warn("Could not update question progress for working question id {}",
                                        question.getId());
                            } else if (CommonUtil.COMPLETED.equals(completed)) {
                                complete(question);
                            }
                        });
            } else {
                //FIXME maybe remove method and move this to catch -> see refactoring resolution() in StackClient
                DefaultQuestionStrategy.LOGGER.warn("Working question {} has been aborted because the original question was deleted", question);
                abort(question);
            }
        } catch (StackResourceNotFoundException e) {
            DefaultQuestionStrategy.LOGGER.warn("Working question {} has been aborted due to {}", question, e);
            abort(question);
        }
    }

    /**
     * Actions taken when question completed:
     * - add new history entry
     * - remove working question entry
     * - update user data (active questions counter)
     *
     * @param question current question
     */
    private void complete(DefaultWorkingQuestion question) {
        // remove from `/workingQuestions` and add to history
        database.getReference(paths.history(question.getId()))
                .setValueAsync(new HistoryEntryDO<>(question, ProgressType.COMPLETED));
        database.getReference(paths.resolve(this).workingQuestions(question.getId()))
                .removeValueAsync();
        // update user data
        database.getReference(paths.users(question.getUser_id()))
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot snapshot) {
                        UserDO user = snapshot.getValue(UserDO.class);
                        int activeQuestions = user.getActiveQuestions();
                        database.getReference(paths.usersActiveQuestions(question.getUser_id()))
                                .setValueAsync(activeQuestions - 1);
                    }
                    @Override
                    public void onCancelled(DatabaseError error) {
                        DefaultQuestionStrategy.LOGGER.warn("Could not update activeQuestions user data for working"
                                + " question id {} error {}", question.getId(), error);
                    }
                });
    }

    /**
     * DefaultQuestion probably meanwhile deleted -> abort & move to history
     *
     * @param question current question
     */
    private void abort(DefaultWorkingQuestion question) {
        database.getReference(paths.history(question.getId()))
                .setValueAsync(new HistoryEntryDO<>(question, ProgressType.ABORTED));
        database.getReference(paths.resolve(this).questionsProgress(question.getId()))
                .setValueAsync(ProgressType.ABORTED);
        database.getReference(paths.resolve(this).workingQuestions(question.getId()))
                .removeValueAsync();
    }

    private Map<String, Object> updateQuestion(DefaultWorkingQuestion question, int currentViews) {
        question.setCurrentViews(currentViews);
        return new MapBuilder()
                .add("clicks", question.incrementClicks())
                .add("currentViews", question.getCurrentViews())
                .build();
    }
}
