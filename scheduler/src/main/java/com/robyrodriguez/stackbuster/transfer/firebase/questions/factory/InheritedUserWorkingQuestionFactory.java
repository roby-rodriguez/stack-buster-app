package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.UserWorkingQuestionDO;

public class InheritedUserWorkingQuestionFactory implements WorkingQuestionFactory<UserQuestionDO, UserWorkingQuestionDO> {

    @Override
    public UserWorkingQuestionDO fromQuestion(final UserQuestionDO question, final int views) {
        return new UserWorkingQuestionDO(question, views);
    }
}
