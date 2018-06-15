package com.robyrodriguez.stackbuster.transfer.firebase.questions.factory;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseQuestion;
import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;

import javax.validation.constraints.NotNull;

public interface WorkingQuestionFactory<Q extends BaseQuestion, W extends BaseWorkingQuestion> {
    W fromQuestion(@NotNull Q question, int views);
}
