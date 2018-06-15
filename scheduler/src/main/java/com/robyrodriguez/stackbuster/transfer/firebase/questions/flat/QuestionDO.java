package com.robyrodriguez.stackbuster.transfer.firebase.questions.flat;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.Question;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

public class QuestionDO implements Question {
    @Override
    public ProgressType getProgress() {
        return null;
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
}
