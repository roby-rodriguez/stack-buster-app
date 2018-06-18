package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.types.BadgeType;

/**
 * Root DO class for all question types
 */
@IgnoreExtraProperties
public abstract class AbstractQuestionDO {

    private String id;
    private BadgeType badgeType;

    public AbstractQuestionDO() {
    }

    public BadgeType getBadgeType() {
        return this.badgeType;
    }

    public void setBadgeType(final BadgeType pBadgeType) {
        badgeType = pBadgeType;
    }

    @Exclude
    public String getId() {
        return this.id;
    }

    @Exclude
    public void setId(final String pId) {
        id = pId;
    }

    @Override
    public String toString() {
        return "{" + "id='" + id + '\'' + ", badgeType=" + badgeType + '}';
    }
}
