package com.robyrodriguez.stackbuster.transfer.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private long created;
    private long lastActive;
    private int activeQuestions;
    private int totalQuestions;
    private String apiKey;
    // only user has apiKey
    private int credit;
    private boolean banned;

    public UserDO() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String pId) {
        id = pId;
    }

    public long getCreated() {
        return this.created;
    }

    public void setCreated(final long pCreated) {
        created = pCreated;
    }

    public long getLastActive() {
        return this.lastActive;
    }

    public void setLastActive(final long pLastActive) {
        lastActive = pLastActive;
    }

    public int getActiveQuestions() {
        return this.activeQuestions;
    }

    public void setActiveQuestions(final int pActiveQuestions) {
        activeQuestions = pActiveQuestions;
    }

    public int getTotalQuestions() {
        return totalQuestions;
    }

    public void setTotalQuestions(final int totalQuestions) {
        this.totalQuestions = totalQuestions;
    }

    public String getApiKey() {
        return this.apiKey;
    }

    public void setApiKey(final String pApiKey) {
        apiKey = pApiKey;
    }

    public int getCredit() {
        return this.credit;
    }

    public void setCredit(final int pCredit) {
        credit = pCredit;
    }

    public boolean isBanned() {
        return banned;
    }

    public void setBanned(final boolean banned) {
        this.banned = banned;
    }

    @Override
    public String toString() {
        return "UserDO{" + "id='" + id + '\'' + ", created=" + created + ", lastActive=" + lastActive
                + ", activeQuestions=" + activeQuestions + ", totalQuestions=" + totalQuestions + ", apiKey='" + apiKey
                + '\'' + ", credit=" + credit + ", banned=" + banned + '}';
    }
}
