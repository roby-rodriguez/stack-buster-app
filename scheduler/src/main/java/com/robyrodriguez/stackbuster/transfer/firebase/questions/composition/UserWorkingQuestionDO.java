package com.robyrodriguez.stackbuster.transfer.firebase.questions.composition;

import com.google.firebase.database.IgnoreExtraProperties;
import com.robyrodriguez.stackbuster.service.worker.visitor.IncrementStrategyVisitor;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.contract.QuestionWrapper;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.structure.BaseWorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.UserWorkingQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;
import com.robyrodriguez.stackbuster.types.BadgeType;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * User working question items at `/workingQuestions/user`
 */
@IgnoreExtraProperties
public class UserWorkingQuestionDO implements UserWorkingQuestion, QuestionWrapper<BaseWorkingQuestionDO> {

    private BaseWorkingQuestionDO t;
    private String uid;
    private List<String> addresses = new ArrayList<>();

    public UserWorkingQuestionDO() {
    }

    public UserWorkingQuestionDO(@NotNull UserQuestionDO userQuestion, int viewsCreated) {
        t = new BaseWorkingQuestionDO(userQuestion.getBadgeType(), viewsCreated);
        uid = userQuestion.getUid();
    }

    @Override
    public BaseWorkingQuestionDO getT() {
        return t;
    }

    @Override
    public void setT(BaseWorkingQuestionDO t) {
        this.t = t;
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
    public int incrementClicks() {
        return t.incrementClicks();
    }

    @Override
    public String getId() {
        return t.getT().getId();
    }

    @Override
    public void setId(final String pId) {
        t.getT().setId(pId);
    }

    @Override
    public String getUser_id() {
        return t.getT().getUser_id();
    }

    @Override
    public void setUser_id(final String user_id) {
        t.getT().setUser_id(user_id);
    }

    @Override
    public BadgeType getBadgeType() {
        return t.getT().getBadgeType();
    }

    @Override
    public int getClicks() {
        return t.getClicks();
    }

    @Override
    public void accept(final IncrementStrategyVisitor visitor) throws Exception {
        visitor.visit(this);
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
