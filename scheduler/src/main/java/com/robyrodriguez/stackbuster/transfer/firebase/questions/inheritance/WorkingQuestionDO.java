package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.WorkingQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;

/**
 * Question items at `/workingQuestions`
 */
@IgnoreExtraProperties
public class WorkingQuestionDO extends AbstractWorkingQuestionDO implements WorkingQuestion {

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

    public void setCurrentViews(int currentViews) {
        this.currentViews = currentViews;
    }

    @Override
    public void accept(final IncrementStrategyVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "WorkingQuestionDO{" + "currentViews=" + currentViews + ", " + super.toString() + "} ";
    }
}
