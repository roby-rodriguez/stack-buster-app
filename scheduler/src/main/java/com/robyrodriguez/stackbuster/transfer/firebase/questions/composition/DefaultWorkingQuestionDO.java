package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure.BaseWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultWorkingQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;

import javax.validation.constraints.NotNull;

/**
 * Working question items at `/workingQuestions/default`
 */
@IgnoreExtraProperties
public class DefaultWorkingQuestionDO implements DefaultWorkingQuestion {

    private BaseWorkingQuestionDO t;
    private int currentViews;

    public DefaultWorkingQuestionDO() {
    }

    public DefaultWorkingQuestionDO(@NotNull DefaultQuestionDO question, int viewsCreated) {
        t = new BaseWorkingQuestionDO(question.getBadgeType(), viewsCreated);
        currentViews = viewsCreated;
    }

    public BaseWorkingQuestionDO getT() {
        return t;
    }

    public void setT(BaseWorkingQuestionDO t) {
        this.t = t;
    }

    @Override
    public int incrementClicks() {
        return t.incrementClicks();
    }

    @Override
    public String getId() {
        return t.getT().getId();
    }

    @Override
    public void setId(final String id) {
        t.getT().setId(id);
    }

    @Override
    public String getUser_id() {
        return t.getT().getUser_id();
    }

    @Override
    public void setUser_id(final String user_id) {
        t.getT().setUser_id(user_id);
    }

    @Override
    public BadgeType getBadgeType() {
        return t.getT().getBadgeType();
    }

    @Override
    public int getCurrentViews() {
        return currentViews;
    }

    @Override
    public void setCurrentViews(int currentViews) {
        this.currentViews = currentViews;
    }

    @Override
    public void accept(final IncrementStrategyVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "DefaultWorkingQuestionDO{" + "currentViews=" + currentViews + ", " + t + "}";
    }
}
