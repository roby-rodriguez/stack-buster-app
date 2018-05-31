package com.robyrodriguez.stackbuster.transfer.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Question items at `/workingQuestions`
 */
@IgnoreExtraProperties
public class WorkingQuestionDO extends AbstractWorkingQuestionDO {

    private int currentViews;

    public WorkingQuestionDO() {
    }

    public WorkingQuestionDO(QuestionDO question, int viewsCreated) {
        super(question, viewsCreated);
        this.currentViews = viewsCreated;
    }

    public int getCurrentViews() {
        return currentViews;
    }

    public void setCurrentViews(final int currentViews) {
        this.currentViews = currentViews;
    }

    @Override
    public String toString() {
        return "WorkingQuestionDO{" + "currentViews=" + currentViews + ", " + super.toString() + "} ";
    }
}
