package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.WorkingQuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.inheritance.QuestionDO;

public class InheritedWorkingQuestionFactory implements WorkingQuestionFactory<QuestionDO, WorkingQuestionDO> {

    @Override
    public WorkingQuestionDO fromQuestion(final QuestionDO question, final int views) {
        return new WorkingQuestionDO(question, views);
    }
}
