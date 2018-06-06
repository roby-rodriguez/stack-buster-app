package com.robyrodriguez.stackbuster.transfer.firebase_new.structure;

import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

import java.util.Date;
import javax.validation.constraints.NotNull;

public final class BaseWorkingQuestionDO {

    private long created;
    private int viewsCreated;
    private int clicks;
    private ProgressType progress;
    private BaseQuestionDO t;

    public BaseWorkingQuestionDO() {
        t = new BaseQuestionDO();
        created = new Date().getTime();
        progress = ProgressType.IN_PROGRESS;
    }

    public BaseWorkingQuestionDO(@NotNull BadgeType badgeType, int views) {
        this();
        t.setBadgeType(badgeType);
        viewsCreated = views;
    }

    public BaseQuestionDO getT() {
        return t;
    }

    public void setT(BaseQuestionDO t) {
        this.t = t;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public int getViewsCreated() {
        return viewsCreated;
    }

    public void setViewsCreated(int viewsCreated) {
        this.viewsCreated = viewsCreated;
    }

    public int getClicks() {
        return clicks;
    }

    public void setClicks(int clicks) {
        this.clicks = clicks;
    }

    public ProgressType getProgress() {
        return progress;
    }

    public void setProgress(ProgressType progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "{" +
                "created=" + created +
                ", viewsCreated=" + viewsCreated +
                ", clicks=" + clicks +
                ", progress=" + progress +
                ", " + t +
                '}';
    }
}
