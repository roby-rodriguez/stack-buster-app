package com.robyrodriguez.stackbuster.transfer.firebase_new;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase_new.structure.BaseWorkingQuestionDO;

import javax.validation.constraints.NotNull;

/**
 * Question items at `/workingQuestions/default`
 */
@IgnoreExtraProperties
public class WorkingQuestionDO {

    private BaseWorkingQuestionDO t;
    private int currentViews;

    public WorkingQuestionDO() {
    }

    public WorkingQuestionDO(@NotNull QuestionDO question, int viewsCreated) {
        t = new BaseWorkingQuestionDO(question.getBadgeType(), viewsCreated);
        currentViews = viewsCreated;
    }

    public int getCurrentViews() {
        return currentViews;
    }

    public void setCurrentViews(final int currentViews) {
        this.currentViews = currentViews;
    }

    @Override
    public String toString() {
        return "WorkingQuestionDO{" + "currentViews=" + currentViews + ", " + t + "}";
    }
}
