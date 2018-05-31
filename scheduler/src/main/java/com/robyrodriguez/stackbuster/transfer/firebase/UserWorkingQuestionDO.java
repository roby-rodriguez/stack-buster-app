package com.robyrodriguez.stackbuster.transfer.firebase;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * User-question items at `/workingQuestions`
 */
@IgnoreExtraProperties
public class UserWorkingQuestionDO extends AbstractWorkingQuestionDO {

    private String uid;
    private List<String> addresses = new ArrayList<>();

    public UserWorkingQuestionDO() {
    }

    public UserWorkingQuestionDO(UserQuestionDO userQuestion, int viewsCreated) {
        super(userQuestion, viewsCreated);
        this.setUid(userQuestion.getUid());
        this.addresses = new ArrayList<>();
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
        return "UserWorkingQuestionDO{" + "uid='" + uid + '\'' + ", addresses=" + addresses + ", " + super.toString() + "} ";
    }
}
