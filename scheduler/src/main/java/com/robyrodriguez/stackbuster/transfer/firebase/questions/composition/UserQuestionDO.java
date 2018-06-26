package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.UserQuestion;

/**
 * User question items at `/questions/user`
 */
public class UserQuestionDO extends DefaultQuestionDO implements UserQuestion {

    private String uid;

    public UserQuestionDO() {
        super();
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
