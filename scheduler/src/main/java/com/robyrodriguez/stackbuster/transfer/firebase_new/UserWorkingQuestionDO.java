package com.robyrodriguez.stackbuster.transfer.firebase_new;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.transfer.firebase_new.structure.BaseWorkingQuestionDO;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * Question items at `/workingQuestions/user`
 */
@IgnoreExtraProperties
public class UserWorkingQuestionDO {

    private BaseWorkingQuestionDO t;
    private String uid;
    private List<String> addresses = new ArrayList<>();

    public UserWorkingQuestionDO() {
    }

    public UserWorkingQuestionDO(@NotNull UserQuestionDO userQuestion, int viewsCreated) {
        t = new BaseWorkingQuestionDO(userQuestion.getBadgeType(), viewsCreated);
        uid = userQuestion.getUid();
    }

    public String getUid() {
        return uid;
    }

    public void setUid(final String uid) {
        this.uid = uid;
    }

    public List<String> getAddresses() {
        return addresses;
    }

    public void setAddresses(final List<String> addresses) {
        this.addresses = addresses;
    }

    @Override
    public String toString() {
        return "UserWorkingQuestionDO{" +
                t +
                ", uid='" + uid + '\'' +
                ", addresses=" + addresses +
                '}';
    }
}
