package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseQuestion;

/**
 * Question items at `/questions`
 */
@IgnoreExtraProperties
public class UserQuestionDO extends QuestionDO implements BaseQuestion {

    private String uid;

    public UserQuestionDO() {
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
