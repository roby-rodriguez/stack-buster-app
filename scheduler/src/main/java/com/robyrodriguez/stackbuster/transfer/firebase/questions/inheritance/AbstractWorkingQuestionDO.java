package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.util.Date;

/**
 * Root DO class for all working question types
 */
@IgnoreExtraProperties
public abstract class AbstractWorkingQuestionDO extends AbstractQuestionDO {

    private long created;
    private int viewsCreated;
    private int clicks;
    private String user_id;

    public AbstractWorkingQuestionDO() {
    }

    public AbstractWorkingQuestionDO(QuestionDO question, int viewsCreated) {
        this.setId(question.getId());
        this.setBadgeType(question.getBadgeType());
        this.setUser_id(question.getUser_id());
        this.created = new Date().getTime();
        this.clicks = 0;
        this.viewsCreated = viewsCreated;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(final long created) {
        this.created = created;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(final int clicks) {
        this.clicks = clicks;
    }

    public int incrementClicks() {
        return ++clicks;
    }

    public int getViewsCreated() {
        return viewsCreated;
    }

    public void setViewsCreated(final int viewsCreated) {
        this.viewsCreated = viewsCreated;
    }

    @Exclude
    public String getUser_id() {
        return user_id;
    }

    @Exclude
    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    @Override
    public String toString() {
        return "{" + "created=" + created + ", viewsCreated=" + viewsCreated + ", clicks="
                + clicks + ", " + super.toString() + "} ";
    }
}
