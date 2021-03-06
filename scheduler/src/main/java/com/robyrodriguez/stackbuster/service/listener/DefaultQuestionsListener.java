package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.robyrodriguez.stackbuster.api.StackApi;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.client.DefaultClient;
import com.robyrodriguez.stackbuster.constants.DatabasePaths;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultWorkingQuestion;
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
public class DefaultQuestionsListener<Q extends DefaultQuestion, W extends DefaultWorkingQuestion> implements
        FirebaseListener<Q> {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultQuestionsListener.class);

    @Autowired
    private DefaultClient defaultClient;

    @Autowired
    private DatabasePaths paths;

    @Autowired
    private FirebaseDatabase database;

    @Autowired
    private StackBusterCache cache;

    private Class<Q> qClass;
    private WorkingQuestionFactory<Q, W> workingQuestionFactory;

    public DefaultQuestionsListener(Class<Q> qClass, WorkingQuestionFactory<Q, W> workingQuestionFactory) {
        this.qClass = qClass;
        this.workingQuestionFactory = workingQuestionFactory;
    }

    @Override
    public void process(Q question) {
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
                        database.getReference(paths.resolve(this).workingQuestions(question.getId()))
                                .setValueAsync(workingQuestionFactory.fromQuestion(question, stackQuestion.getView_count()));
                    }
                } else {
                    cleanup(question);
                }
            }
        } catch (BadRequestException bre) {
            cleanup(question);
        } catch (Exception e) {
            DefaultQuestionsListener.LOGGER.error("Encountered error during sanitize {}", e);
        }
    }
    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        Q question = dataSnapshot.getValue(qClass);
        question.setId(dataSnapshot.getKey());

        process(question);
    }
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        DefaultQuestionsListener.LOGGER.info("onChildChanged called on '{}' with args key={} value={}",
                paths.resolve(this).root("questions"), dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        DefaultQuestionsListener.LOGGER.info("onChildRemoved called on '{}' with args key={} value={}",
                paths.resolve(this).root("questions"), dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        DefaultQuestionsListener.LOGGER.warn("onChildMoved called on '{}' with args key={} value={}",
                paths.resolve(this).root("questions"), dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        DefaultQuestionsListener.LOGGER.warn("onCancelled called on '{}' with error={}",
                paths.resolve(this).root("questions"), databaseError);
    }
    private void cleanup(Q question) {
        database.getReference(paths.resolve(this).questions(question.getId())).removeValueAsync();
        cache.delete(question.getId());
        DefaultQuestionsListener.LOGGER.info("Cleanup garbage question {}", question);
    }
}
