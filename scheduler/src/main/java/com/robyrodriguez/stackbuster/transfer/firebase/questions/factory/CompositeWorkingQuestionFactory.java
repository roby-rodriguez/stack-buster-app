package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.composition.WorkingQuestionDO;

public class CompositeWorkingQuestionFactory implements WorkingQuestionFactory<QuestionDO, WorkingQuestionDO> {

    @Override
    public WorkingQuestionDO fromQuestion(final QuestionDO question, final int views) {
        return new WorkingQuestionDO(question, views);
    }
}
