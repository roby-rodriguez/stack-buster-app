package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.DefaultQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.DefaultWorkingQuestionDO;

public class CompositeWorkingQuestionFactory implements WorkingQuestionFactory<DefaultQuestionDO, DefaultWorkingQuestionDO> {

    @Override
    public DefaultWorkingQuestionDO fromQuestion(final DefaultQuestionDO question, final int views) {
        return new DefaultWorkingQuestionDO(question, views);
    }
}
