package com.robyrodriguez.stackbuster.transfer.firebase.questions.flat;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.UserWorkingQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.DefaultWorkingQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Working (& user-working) questions items at `/workingQuestions`
 */
@IgnoreExtraProperties
public class WorkingQuestionDO implements DefaultWorkingQuestion, UserWorkingQuestion {

    private String id;
    private BadgeType badgeType;
    private long created;
    private int viewsCreated;
    private int clicks;
    private String user_id;
    private int currentViews;
    private String uid;
    private List<String> addresses = new ArrayList<>();

    public WorkingQuestionDO() {
    }

    public WorkingQuestionDO(QuestionDO question, int viewsCreated) {
        this.id = question.getId();
        this.badgeType = question.getBadgeType();
        this.user_id = question.getUser_id();
        this.created = new Date().getTime();
        this.clicks = 0;
        this.viewsCreated = viewsCreated;
        this.uid = question.getUid();
        this.addresses = new ArrayList<>();
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(final long created) {
        this.created = created;
    }

    public int getViewsCreated() {
        return viewsCreated;
    }

    public void setViewsCreated(final int viewsCreated) {
        this.viewsCreated = viewsCreated;
    }

    @Override
    public int getClicks() {
        return clicks;
    }

    public void setClicks(final int clicks) {
        this.clicks = clicks;
    }

    @Override
    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    @Override
    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public int getCurrentViews() {
        return currentViews;
    }

    @Override
    public void setCurrentViews(final int currentViews) {
        this.currentViews = currentViews;
    }

    @Override
    public int incrementClicks() {
        return ++clicks;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void setId(final String id) {
        this.id = id;
    }

    @Override
    public String getUser_id() {
        return user_id;
    }

    @Override
    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    @Override
    public BadgeType getBadgeType() {
        return badgeType;
    }

    public void setBadgeType(final BadgeType badgeType) {
        this.badgeType = badgeType;
    }

    @Override
    public void accept(final IncrementStrategyVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "WorkingQuestionDO{" + "id='" + id + '\'' + ", badgeType=" + badgeType + ", created=" + created
                + ", viewsCreated=" + viewsCreated + ", clicks=" + clicks + ", user_id='" + user_id + '\''
                + ", currentViews=" + currentViews + ", uid='" + uid + '\'' + ", addresses=" + addresses + '}';
    }
}
