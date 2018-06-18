package com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.UserWorkingQuestion;

import java.util.ArrayList;
import java.util.List;

/**
 * Working user question items at `/workingQuestions/user`
 */
@IgnoreExtraProperties
public class UserWorkingQuestionDO extends AbstractWorkingQuestionDO implements UserWorkingQuestion {

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
    public void accept(final IncrementStrategyVisitor visitor) throws Exception {
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return "UserWorkingQuestionDO{" + "uid='" + uid + '\'' + ", addresses=" + addresses + ", " + super.toString() + "} ";
    }
}
