package com.robyrodriguez.stackbuster.transfer.firebase;

import java.io.Serializable;
import java.util.Date;

public class UserDO implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private Date created;
    private Date lastActive;
    private int activeQuestions;
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

    public Date getCreated() {
        return this.created;
    }

    public void setCreated(final Date pCreated) {
        created = pCreated;
    }

    public Date getLastActive() {
        return this.lastActive;
    }

    public void setLastActive(final Date pLastActive) {
        lastActive = pLastActive;
    }

    public int getActiveQuestions() {
        return this.activeQuestions;
    }

    public void setActiveQuestions(final int pActiveQuestions) {
        activeQuestions = pActiveQuestions;
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
                + ", activeQuestions=" + activeQuestions + ", apiKey='" + apiKey + '\'' + ", credit=" + credit
                + ", banned=" + banned + '}';
    }
}
