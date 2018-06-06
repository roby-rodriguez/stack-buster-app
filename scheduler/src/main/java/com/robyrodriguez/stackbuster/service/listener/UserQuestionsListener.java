package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.transfer.firebase.UserQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.UserWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.AbstractStackItemWrapperDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionWrapperDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackUserWrapperDO;
import com.robyrodriguez.stackbuster.types.ProgressType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.BadRequestException;

/**
 * Checks if new items added at `/questions/user` are valid and manages corresponding `/workingQuestions/user`
 */
@Component
public class UserQuestionsListener implements ChildEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserQuestionsListener.class);

    @Autowired
    private DefaultClient defaultClient;

    @Autowired
    private FirebaseDatabase database;

    @Autowired
    private StackBusterCache cache;

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        UserQuestionDO question = dataSnapshot.getValue(UserQuestionDO.class);
        question.setId(dataSnapshot.getKey());

        try {
            if (ProgressType.isPending(question.getProgress()) && cache.get(question) == null) {
                // lookup original question and in case it does not exist, remove this "question" (garbage)
                AbstractStackItemWrapperDO<StackQuestionDO> questionWrapper = defaultClient
                        .get(StackApi.QUESTION(question.getId()), StackQuestionWrapperDO.class);

                if (questionWrapper.getItems().size() > 0) {
                    StackQuestionDO stackQuestion = questionWrapper.getItems().get(0);
                    AbstractStackItemWrapperDO userWrapper = defaultClient
                            .get(StackApi.USER(question.getUid()), StackUserWrapperDO.class);

                    if (userWrapper.getItems().size() > 0) {
                        database.getReference("/workingQuestions/user/" + question.getId())
                                .setValueAsync(new UserWorkingQuestionDO(question, stackQuestion.getView_count()));
                    } else {
                        database.getReference("/questions/user/" + question.getId()).removeValueAsync();
                    }

                } else {
                    // TODO extract these strings to constant/functions
                    database.getReference("/questions/user/" + question.getId()).removeValueAsync();
                    UserQuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
                }
            }
        } catch (BadRequestException bre) {
            database.getReference("/questions/user/" + question.getId()).removeValueAsync();
            UserQuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
        } catch (Exception e) {
            UserQuestionsListener.LOGGER.error("Encountered error during sanitize {}", e);
        }
    }
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        UserQuestionsListener.LOGGER.info("onChildChanged called on '/questions/user' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        UserQuestionsListener.LOGGER.info("onChildRemoved called on '/questions/user' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        UserQuestionsListener.LOGGER.warn("onChildMoved called on '/questions/user' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        UserQuestionsListener.LOGGER.warn("onCancelled called on '/questions/user' with error={}", databaseError);
    }
}
