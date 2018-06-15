package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.UserWorkingQuestionDO;

public class CompositeUserWorkingQuestionFactory implements WorkingQuestionFactory<UserQuestionDO, UserWorkingQuestionDO> {

    @Override
    public UserWorkingQuestionDO fromQuestion(final UserQuestionDO question, final int views) {
        return new UserWorkingQuestionDO(question, views);
    }
}
