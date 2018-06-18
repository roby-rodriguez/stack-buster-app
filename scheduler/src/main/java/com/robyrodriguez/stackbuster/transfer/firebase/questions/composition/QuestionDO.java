package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure.BaseQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.Question;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * Question items at `/questions/default`
 */
@IgnoreExtraProperties
public class QuestionDO implements Question {

    private BaseQuestionDO t;
    private String completed;

    public QuestionDO() {
        t = new BaseQuestionDO();
    }

    @Exclude
    @Override
    public String getId() {
        return t.getId();
    }

    @Exclude
    @Override
    public void setId(final String pId) {
        t.setId(pId);
    }

    @Exclude
    @Override
    public BadgeType getBadgeType() {
        return t.getBadgeType();
    }

    @Exclude
    @Override
    public String getUser_id() {
        return t.getUser_id();
    }

    @Exclude
    @Override
    public void setUser_id(final String user_id) {
        t.setUser_id(user_id);
    }

    @Exclude
    @Override
    public ProgressType getProgress() {
        return t.getProgress();
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    public BaseQuestionDO getT() {
        return t;
    }

    public void setT(final BaseQuestionDO t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "QuestionDO{" + "completed='" + completed + "\' " + t + "}";
    }
}
