package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.transfer.firebase.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.WorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.AbstractStackItemWrapperDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionWrapperDO;
import com.robyrodriguez.stackbuster.types.ProgressType;
import com.robyrodriguez.stackbuster.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;

/**
 * Checks if new items added at `/questions/default` are valid and manages corresponding `/workingQuestions/default`
 */
@Component
public class QuestionsListener implements ChildEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionsListener.class);

    @Autowired
    private DefaultClient defaultClient;

    @Autowired
    private FirebaseDatabase database;

    @Autowired
    private StackBusterCache cache;

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        QuestionDO question = dataSnapshot.getValue(QuestionDO.class);
        question.setId(dataSnapshot.getKey());

        try {
            if (ProgressType.isPending(question.getProgress()) && cache.get(question) == null) {
                // lookup original question and in case it does not exist, remove this "question" (garbage)
                AbstractStackItemWrapperDO<StackQuestionDO> questionWrapper = defaultClient
                        .get(StackApi.QUESTION(question.getId()), StackQuestionWrapperDO.class);

                if (questionWrapper.getItems().size() > 0) {
                    StackQuestionDO stackQuestion = questionWrapper.getItems().get(0);
                    String completed = CommonUtil.getCompletionPercentage(stackQuestion.getView_count(),
                            question.getBadgeType().getClicks());

                    if (CommonUtil.COMPLETED.equals(completed)) {
                        // if question already has required number of views - reject it
                        database.getReference("/questions/default/" + question.getId()).removeValueAsync();
                        QuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
                    } else {
                        // otherwise add to working questions for processing
                        database.getReference("/workingQuestions/default/" + question.getId())
                                .setValueAsync(new WorkingQuestionDO(question, stackQuestion.getView_count()));
                    }
                } else {
                    // TODO extract these strings to constant/functions
                    database.getReference("/questions/default/" + question.getId()).removeValueAsync();
                    QuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
                }
            }
        } catch (BadRequestException bre) {
            database.getReference("/questions/default/" + question.getId()).removeValueAsync();
            QuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
        } catch (Exception e) {
            QuestionsListener.LOGGER.error("Encountered error during sanitize {}", e);
        }
    }
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        QuestionsListener.LOGGER.info("onChildChanged called on '/questions/default' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        QuestionsListener.LOGGER.info("onChildRemoved called on '/questions/default' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        QuestionsListener.LOGGER.warn("onChildMoved called on '/questions/default' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        QuestionsListener.LOGGER.warn("onCancelled called on '/questions/default' with error={}", databaseError);
    }
}
