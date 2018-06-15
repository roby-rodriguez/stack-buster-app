package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.QuestionDO;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.flat.WorkingQuestionDO;

public class FlatWorkingQuestionFactory implements WorkingQuestionFactory<QuestionDO, WorkingQuestionDO> {

    @Override
    public WorkingQuestionDO fromQuestion(final QuestionDO question, final int views) {
        return null;
    }
}
