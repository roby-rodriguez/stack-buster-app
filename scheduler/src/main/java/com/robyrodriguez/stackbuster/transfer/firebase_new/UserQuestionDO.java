package com.robyrodriguez.stackbuster.transfer.firebase_new;

/**
 * Question items at `/questions/user`
 */
public class UserQuestionDO extends QuestionDO {

    private String uid;

    public UserQuestionDO() {
        super();
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
