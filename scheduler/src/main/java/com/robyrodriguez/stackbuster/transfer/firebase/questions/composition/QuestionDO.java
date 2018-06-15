package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure.BaseQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * Question items at `/questions/default`
 */
@IgnoreExtraProperties
public class QuestionDO implements BaseQuestion {

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

    @Override
    public BadgeType getBadgeType() {
        return t.getBadgeType();
    }

    @Override
    public String getUser_id() {
        return t.getUser_id();
    }

    @Override
    public void setUser_id(final String user_id) {
        t.setUser_id(user_id);
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "QuestionDO{" + "completed='" + completed + "\' " + t + "}";
    }
}
