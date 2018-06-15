package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.contract.QuestionWrapper;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure.BaseWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.WorkingQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;

import javax.validation.constraints.NotNull;

/**
 * Question items at `/workingQuestions/default`
 */
@IgnoreExtraProperties
public class WorkingQuestionDO implements WorkingQuestion, QuestionWrapper<BaseWorkingQuestionDO> {

    private BaseWorkingQuestionDO t;
    private int currentViews;

    public WorkingQuestionDO() {
    }

    public WorkingQuestionDO(@NotNull QuestionDO question, int viewsCreated) {
        t = new BaseWorkingQuestionDO(question.getBadgeType(), viewsCreated);
        currentViews = viewsCreated;
    }

    @Override
    public BaseWorkingQuestionDO getT() {
        return t;
    }

    @Override
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
    public void setId(final String pId) {
        t.getT().setId(pId);
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
        return "WorkingQuestionDO{" + "currentViews=" + currentViews + ", " + t + "}";
    }
}
