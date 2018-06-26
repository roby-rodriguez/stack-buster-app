package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Delegate questions listener (for flat structured hierarchy)
 */
@Configurable
public class QuestionsListener implements ChildEventListener {

    @Autowired
    private DefaultQuestionsListener<QuestionDO, WorkingQuestionDO> defaultQuestionsListener;

    @Autowired
    private UserQuestionsListener<QuestionDO, WorkingQuestionDO> userQuestionsListener;

    public QuestionsListener() {}

    @Override
    public void onChildAdded(final DataSnapshot dataSnapshot, final String s) {
        QuestionDO question = dataSnapshot.getValue(QuestionDO.class);
        question.setId(dataSnapshot.getKey());

        if (question.getUid() == null) {
            defaultQuestionsListener.process(question);
        } else {
            userQuestionsListener.process(question);
        }
    }
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        defaultQuestionsListener.onChildChanged(dataSnapshot, s);
    }
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        defaultQuestionsListener.onChildRemoved(dataSnapshot);
    }
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        defaultQuestionsListener.onChildMoved(dataSnapshot, s);
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        defaultQuestionsListener.onCancelled(databaseError);
    }
}
