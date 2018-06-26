package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.UserQuestion;

/**
 * User question items at `/questions/user`
 */
@IgnoreExtraProperties
public class UserQuestionDO extends DefaultQuestionDO implements UserQuestion {

    private String uid;

    public UserQuestionDO() {
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
        return "UserQuestionDO{" + "uid='" + uid + "\' " + super.toString() + "}";
    }
}
