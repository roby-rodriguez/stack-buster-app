package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * Root DO class for all question types
 */
@IgnoreExtraProperties
public final class BaseQuestionDO {

    private String id;
    private BadgeType badgeType;
    private String user_id;
    private ProgressType progress;

    public BaseQuestionDO() {
    }

    @Exclude
    public String getId() {
        return this.id;
    }

    @Exclude
    public void setId(final String pId) {
        id = pId;
    }

    public BadgeType getBadgeType() {
        return this.badgeType;
    }

    public void setBadgeType(final BadgeType pBadgeType) {
        badgeType = pBadgeType;
    }

    @Exclude
    public String getUser_id() {
        return user_id;
    }

    @Exclude
    public void setUser_id(final String user_id) {
        this.user_id = user_id;
    }

    public ProgressType getProgress() {
        return progress;
    }

    public void setProgress(final ProgressType progress) {
        this.progress = progress;
    }

    @Override
    public String toString() {
        return "{" +
                "id='" + id + '\'' +
                ", badgeType=" + badgeType +
                ", user_id='" + user_id + '\'' +
                '}';
    }
}
