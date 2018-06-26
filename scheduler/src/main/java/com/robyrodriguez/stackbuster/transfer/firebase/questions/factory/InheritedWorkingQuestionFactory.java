package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.DefaultQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.DefaultWorkingQuestionDO;

public class InheritedWorkingQuestionFactory implements WorkingQuestionFactory<DefaultQuestionDO, DefaultWorkingQuestionDO> {

    @Override
    public DefaultWorkingQuestionDO fromQuestion(final DefaultQuestionDO question, final int views) {
        return new DefaultWorkingQuestionDO(question, views);
    }
}
