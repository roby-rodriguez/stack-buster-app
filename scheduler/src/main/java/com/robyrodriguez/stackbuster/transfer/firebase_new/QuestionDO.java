package com.robyrodriguez.stackbuster.transfer.firebase_new;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase_new.structure.BaseQuestionDO;
import com.robyrodriguez.stackbuster.types.BadgeType;

/**
 * Question items at `/questions/default`
 */
@IgnoreExtraProperties
public class QuestionDO {

    private BaseQuestionDO base;
    private String completed;

    public QuestionDO() {
        base = new BaseQuestionDO();
    }

    @Exclude
    public String getId() {
        return base.getId();
    }

    @Exclude
    public void setId(final String pId) {
        base.setId(pId);
    }

    public BadgeType getBadgeType() {
        return base.getBadgeType();
    }

    public void setBadgeType(final BadgeType pBadgeType) {
        base.setBadgeType(pBadgeType);
    }

    public String getUser_id() {
        return base.getUser_id();
    }

    public void setUser_id(final String user_id) {
        base.setUser_id(user_id);
    }

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(String completed) {
        this.completed = completed;
    }

    @Override
    public String toString() {
        return "QuestionDO{" + "completed='" + completed + "\' " + base + "}";
    }
}
