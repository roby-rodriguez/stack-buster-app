package com.robyrodriguez.stackbuster.transfer.firebase.questions.flat;

import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.WorkingQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;

public class WorkingQuestionDO implements WorkingQuestion {

    @Override
    public int getCurrentViews() {
        return 0;
    }

    @Override
    public void setCurrentViews(final int currentViews) {

    }

    @Override
    public int incrementClicks() {
        return 0;
    }

    @Override
    public String getId() {
        return null;
    }

    @Override
    public void setId(final String pId) {

    }

    @Override
    public String getUser_id() {
        return null;
    }

    @Override
    public void setUser_id(final String user_id) {

    }

    @Override
    public BadgeType getBadgeType() {
        return null;
    }

    @Override
    public void accept(final IncrementStrategyVisitor visitor) throws Exception {
        visitor.visit(this);
    }
}
