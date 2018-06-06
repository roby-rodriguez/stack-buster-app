package com.robyrodriguez.stackbuster.service.listener;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.robyrodriguez.stackbuster.cache.StackBusterCache;
import com.robyrodriguez.stackbuster.transfer.firebase.AbstractWorkingQuestionDO;
import com.robyrodriguez.stackbuster.types.ProgressType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Reads out data from `/workingQuestions` node and stores them in cache
 */
public class WorkingQuestionsListener<T extends AbstractWorkingQuestionDO> implements ChildEventListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(WorkingQuestionsListener.class);

    private StackBusterCache cache;

    private Class<T> tClass;

    public WorkingQuestionsListener(StackBusterCache cache, Class<T> tClass) {
        this.cache = cache;
        this.tClass = tClass;
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
        T question = dataSnapshot.getValue(tClass);
        question.setId(dataSnapshot.getKey());
        cache.set(question);
    }
}
