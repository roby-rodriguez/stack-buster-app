package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure.BaseQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * DefaultQuestion items at `/questions/default`
 */
@IgnoreExtraProperties
public class DefaultQuestionDO implements DefaultQuestion {

    private BaseQuestionDO t;

    public DefaultQuestionDO() {
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

    public BaseQuestionDO getT() {
        return t;
    }

    public void setT(final BaseQuestionDO t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "DefaultQuestionDO{" + t + "}";
    }
}
