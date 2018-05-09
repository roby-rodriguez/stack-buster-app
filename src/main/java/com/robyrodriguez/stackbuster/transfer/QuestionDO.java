package com.robyrodriguez.stackbuster.transfer;

import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class QuestionDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String uid;
    private long created;
    private int viewsCreated;
    private int currentViews;
    private int clicks;
    private BadgeType badgeType;
    private String user_id;
    private ProgressType progress;
    private List<String> addresses;

    public QuestionDO() {
    }

    /**
     * Sanitize constructor
     *
     * @param question user question
     * @param viewsCreated clicks on create
     */
    public QuestionDO(QuestionDO question, int viewsCreated) {
        this.id = question.id;
        this.uid = question.uid;
        this.created = new Date().getTime();
        this.badgeType = question.badgeType;
        this.clicks = 0;
        this.currentViews = this.viewsCreated = viewsCreated;
        this.progress = ProgressType.IN_PROGRESS;
        this.addresses = new ArrayList<>();
    }

    public QuestionDO(int viewsCreated, BadgeType badgeType) {
        this.created = new Date().getTime();
        this.badgeType = badgeType;
        this.clicks = 0;
        this.currentViews = this.viewsCreated = viewsCreated;
        this.progress = ProgressType.IN_PROGRESS;
        this.addresses = new ArrayList<>();
    }

    public long getCreated() {
        return this.created;
    }

    public void setCreated(final long pCreated) {
        created = pCreated;
    }

    public int getViewsCreated() {
        return this.viewsCreated;
    }

    public void setViewsCreated(final int pViews) {
        viewsCreated = pViews;
    }

    public int getCurrentViews() {
        return this.currentViews;
    }

    public void setCurrentViews(final int pCurrentViews) {
        currentViews = pCurrentViews;
    }

    public int getClicks() {
        return this.clicks;
    }

    public void setClicks(final int pCount) {
        clicks = pCount;
    }

    public BadgeType getBadgeType() {
        return this.badgeType;
    }

    public void setBadgeType(final BadgeType pBadgeType) {
        badgeType = pBadgeType;
    }

    public String getUser_id() {
        return this.user_id;
    }

    public void setUser_id(final String pUser_id) {
        user_id = pUser_id;
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(final String pUid) {
        uid = pUid;
    }

    public List<String> getAddresses() {
        return this.addresses;
    }

    public void setAddresses(final List<String> pAddresses) {
        addresses = pAddresses;
    }

    public ProgressType getProgress() {
        return this.progress;
    }

    public void setProgress(final ProgressType pProgress) {
        progress = pProgress;
    }

    @Override
    public String toString() {
        return "QuestionDO{" + "id='" + id + '\'' + ", uid='" + uid + '\'' + ", created=" + created + ", viewsCreated="
                + viewsCreated + ", currentViews=" + currentViews + ", clicks=" + clicks + ", badgeType=" + badgeType
                + ", user_id='" + user_id + '\'' + ", progress=" + progress + ", addresses=" + addresses + '}';
    }
}
