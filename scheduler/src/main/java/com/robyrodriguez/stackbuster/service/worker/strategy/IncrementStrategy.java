package com.robyrodriguez.stackbuster.service.worker.strategy;

import com.robyrodriguez.stackbuster.transfer.firebase.questions.contract.structure.BaseWorkingQuestion;

public interface IncrementStrategy<T extends BaseWorkingQuestion> {
    void execute(T workingQuestion) throws Exception;
}
