package com.robyrodriguez.stackbuster.transfer.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

/**
 * Question items at `/questions`
 */
@IgnoreExtraProperties
public class UserQuestionDO extends QuestionDO {

    private String uid;

    public UserQuestionDO() {
    }

    public UserQuestionDO(UserQuestionDO userQuestion, int viewsCreated) {

    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    @Override
    public String toString() {
        return "UserQuestionDO{" + "uid='" + uid + "\' " + super.toString() + "}";
    }
}
