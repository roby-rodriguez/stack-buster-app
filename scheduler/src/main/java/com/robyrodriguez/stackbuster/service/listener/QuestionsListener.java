package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.Question;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.WorkingQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.factory.WorkingQuestionFactory;
import com.robyrodriguez.stackbuster.transfer.stack_api.AbstractStackItemWrapperDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionDO;
import com.robyrodriguez.stackbuster.transfer.stack_api.StackQuestionWrapperDO;
import com.robyrodriguez.stackbuster.types.ProgressType;
import com.robyrodriguez.stackbuster.utils.CommonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import javax.ws.rs.BadRequestException;

/**
 * Checks if new items added at `/questions/default` are valid and manages corresponding `/workingQuestions/default`
 */
@Configurable
public class QuestionsListener<Q extends Question, W extends WorkingQuestion> implements ChildEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionsListener.class);

    @Autowired
    private DefaultClient defaultClient;

    @Autowired
    private FirebaseDatabase database;

    @Autowired
    private StackBusterCache cache;

    private Class<Q> qClass;
    private WorkingQuestionFactory<Q, W> workingQuestionFactory;

    public QuestionsListener(Class<Q> qClass, WorkingQuestionFactory<Q, W> workingQuestionFactory) {
        this.qClass = qClass;
        this.workingQuestionFactory = workingQuestionFactory;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Q question = dataSnapshot.getValue(qClass);
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
                        cleanup(question);
                    } else {
                        // otherwise add to working questions for processing
                        database.getReference("/workingQuestions/default/" + question.getId())
                                .setValueAsync(workingQuestionFactory.fromQuestion(question, stackQuestion.getView_count()));
                    }
                } else {
                    // TODO extract these strings to constant/functions
                    cleanup(question);
                }
            }
        } catch (BadRequestException bre) {
            cleanup(question);
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
    private void cleanup(Q question) {
        database.getReference("/questions/default/" + question.getId()).removeValueAsync();
        cache.delete(question.getId());
        QuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
    }
}
