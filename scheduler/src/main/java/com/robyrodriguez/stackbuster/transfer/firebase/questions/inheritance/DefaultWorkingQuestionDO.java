package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultWorkingQuestion;

/**
 * DefaultQuestion items at `/workingQuestions/default`
 */
@IgnoreExtraProperties
public class DefaultWorkingQuestionDO extends AbstractWorkingQuestionDO implements DefaultWorkingQuestion {

    private int currentViews;

    public DefaultWorkingQuestionDO() {
    }

    public DefaultWorkingQuestionDO(DefaultQuestionDO question, int viewsCreated) {
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
        return "DefaultWorkingQuestionDO{" + "currentViews=" + currentViews + ", " + super.toString() + "} ";
    }
}
