package com.robyrodriguez.stackbuster.transfer.firebase.questions.flat;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.UserQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;
import com.robyrodriguez.stackbuster.types.ProgressType;

/**
 * Questions (& user-question) items at `/questions`
 */
@IgnoreExtraProperties
public class QuestionDO implements UserQuestion {

    private String id;
    private BadgeType badgeType;
    private String completed;
    private String user_id;
    private ProgressType progress;
    private String uid;

    public QuestionDO() {}

    public String getCompleted() {
        return completed;
    }

    public void setCompleted(final String completed) {
        this.completed = completed;
    }

    public void setProgress(final ProgressType progress) {
        this.progress = progress;
    }

    @Override
    public ProgressType getProgress() {
        return progress;
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
    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "QuestionDO{" + "id='" + id + '\'' + ", badgeType=" + badgeType + ", completed='" + completed + '\''
                + ", user_id='" + user_id + '\'' + ", progress=" + progress + ", uid='" + uid + '\'' + '}';
    }
}
