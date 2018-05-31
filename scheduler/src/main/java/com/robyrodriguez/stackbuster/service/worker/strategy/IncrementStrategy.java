package com.robyrodriguez.stackbuster.service.worker.strategy;

import com.robyrodriguez.stackbuster.transfer.firebase.AbstractWorkingQuestionDO;

public interface IncrementStrategy<T extends AbstractWorkingQuestionDO> {
    void execute(T workingQuestion) throws Exception;
}
