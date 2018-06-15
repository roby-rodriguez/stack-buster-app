package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.WorkingQuestion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

/**
 * Reads out data from `/workingQuestions` node and stores them in cache
 */
@Configurable
public class WorkingQuestionsListener<W extends WorkingQuestion> implements ChildEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingQuestionsListener.class);

    @Autowired
    private StackBusterCache cache;

    private Class<W> wClass;

    public WorkingQuestionsListener(Class<W> wClass) {
        this.wClass = wClass;
    }

    @Override
    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
        updateCache(dataSnapshot);
    }
    @Override
    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        updateCache(dataSnapshot);
    }
    @Override
    public void onChildRemoved(DataSnapshot dataSnapshot) {
        cache.delete(dataSnapshot.getKey());
    }
    @Override
    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        WorkingQuestionsListener.LOGGER.info("onChildMoved called on '/workingQuestions' with args key={} value={}",
                dataSnapshot.getKey(), dataSnapshot.getValue());
    }
    @Override
    public void onCancelled(DatabaseError databaseError) {
        WorkingQuestionsListener.LOGGER.warn("onCancelled called on '/workingQuestions' with error={}", databaseError);
    }
    private void updateCache(DataSnapshot dataSnapshot) {
        W question = dataSnapshot.getValue(wClass);
        question.setId(dataSnapshot.getKey());
        cache.set(question);
    }
}
